package net.myconfig.service.impl;

import static net.myconfig.service.db.SQL.USER_SUMMARIES;
import static net.myconfig.service.db.SQLColumns.ADMIN;
import static net.myconfig.service.db.SQLColumns.DISABLED;
import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.EMAIL;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.NEWPASSWORD;
import static net.myconfig.service.db.SQLColumns.PASSWORD;
import static net.myconfig.service.db.SQLColumns.USER;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.UIService;
import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.api.message.MessageDestination;
import net.myconfig.service.api.message.MessageService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.api.template.TemplateModel;
import net.myconfig.service.api.template.TemplateService;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.TokenType;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.SecurityManagementNotFoundException;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.myconfig.service.token.TokenService;
import net.myconfig.service.validation.UserValidation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractSecurityService implements SecurityService {

	private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	private final ConfigurationService configurationService;
	private final SecuritySelector securitySelector;
	private final MessageService messageService;
	private final UIService uiService;
	private final TokenService tokenService;
	private final TemplateService templateService;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, ConfigurationService configurationService, SecuritySelector securitySelector, MessageService messageService,
			UIService uiService, TokenService tokenService, TemplateService templateService) {
		super(dataSource, validator);
		this.configurationService = configurationService;
		this.securitySelector = securitySelector;
		this.messageService = messageService;
		this.uiService = uiService;
		this.tokenService = tokenService;
		this.templateService = templateService;
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_setup)
	public void setSecurityMode(String mode) {
		logger.info("[security] Changing security mode to {}", mode);
		String currentMode = securitySelector.getSecurityMode();
		if (!StringUtils.equals(currentMode, mode)) {
			List<String> securityModes = securitySelector.getSecurityModes();
			if (!securityModes.contains(mode)) {
				throw new SecurityManagementNotFoundException(mode);
			}
			configurationService.setParameter(ConfigurationKey.SECURITY_MODE, mode);

		} else {
			logger.info("[security] {} mode is already selected.", mode);
		}
	}

	@Override
	@Transactional(readOnly = true)
	@UserGrant(UserFunction.security_users)
	public List<UserSummary> getUserList() {
		List<User> users = getJdbcTemplate().query(USER_SUMMARIES, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString(NAME), rs.getString(DISPLAYNAME), rs.getBoolean(ADMIN), rs.getBoolean(VERIFIED), rs.getBoolean(DISABLED));
			}
		});
		return Lists.transform(users, new Function<User, UserSummary>() {
			@Override
			public UserSummary apply(User user) {
				EnumSet<UserFunction> functions = getUserFunctions(user);
				return new UserSummary(user.getName(), user.getDisplayName(), user.isAdmin(), user.isVerified(), user.isDisabled(), functions);
			}
		});
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userCreate(String name, String displayName, String email) {
		validate(UserValidation.class, NAME, name);
		validate(UserValidation.class, DISPLAYNAME, displayName);
		validate(UserValidation.class, EMAIL, email);
		try {
			// Creates the user
			int count = getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, new MapSqlParameterSource().addValue(NAME, name).addValue(DISPLAYNAME, displayName).addValue(EMAIL, email));
			// Its initial state is not verified and a notification must be sent
			// to the email
			Message message = createNewUserMessage(name, displayName);
			// Sends the message
			Ack ack = messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
			// OK
			return Ack.one(count).and(ack);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException(name);
		}
	}

	private Message createUserMessage(String user, String userDisplayName, TokenType tokenType, UIService.Link linkType, String templateId, String subjectFormat) {
		// Generates a token for the response
		String token = tokenService.generateToken(tokenType, user);
		// Gets the return link
		String link = uiService.getLink(linkType, user, token);
		// Message template model
		TemplateModel model = new TemplateModel();
		model.add("user", user);
		model.add("userFullName", userDisplayName);
		model.add("link", link);
		// Message content
		String content = templateService.generate(templateId, model);
		// Creates the message
		return new Message(String.format(subjectFormat, user), content);
	}

	private Message createNewUserMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.NEW_USER, UIService.Link.NEW_USER, "user_new.txt", "myconfig - registration for account");
	}

	private Message createUserForgottenMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.NEW_USER, UIService.Link.NEW_USER, "user_forgotten.txt", "myconfig - account reset");
	}

	private Message createResetUserMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.RESET_USER, UIService.Link.RESET_USER, "user_reset.txt", "myconfig - reset password for account");
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userDelete(String name) {
		MapSqlParameterSource param = new MapSqlParameterSource(SQLColumns.NAME, name);
		getNamedParameterJdbcTemplate().update(SQL.USER_FUNCTIONS_DELETE, param);
		getNamedParameterJdbcTemplate().update(SQL.APP_FUNCTIONS_DELETE, param);
		getNamedParameterJdbcTemplate().update(SQL.ENV_FUNCTIONS_DELETE, param);
		int count = getNamedParameterJdbcTemplate().update(SQL.USER_DELETE, param);
		return Ack.one(count);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionAdd(String name, UserFunction fn) {
		userFunctionRemove(name, fn);
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_ADD, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionRemove(String name, UserFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_REMOVE, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

	@Override
	public void checkUserConfirm(String name, String token) {
		tokenService.checkToken(token, TokenType.NEW_USER, name);
	}

	@Override
	public void checkUserReset(String name, String token) {
		tokenService.checkToken(token, TokenType.RESET_USER, name);
	}

	@Override
	@Transactional
	public void userReset(String name, String token, String oldPassword, String newPassword) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.RESET_USER, name);
		// Changes the password
		int count = getNamedParameterJdbcTemplate().update(SQL.USER_RESET,
				new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(oldPassword)).addValue(NEWPASSWORD, digest(newPassword)));
		// Check
		if (count != 1) {
			throw new CannotResetUserException(name);
		}
	}

	@Override
	@Transactional
	public void userConfirm(String name, String token, String password) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.NEW_USER, name);
		// Saves the password
		getNamedParameterJdbcTemplate().update(SQL.USER_CONFIRM, new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(password)));
	}

	@Override
	@Transactional
	public void userReset() {
		String name = securitySelector.getCurrentUserName();
		if (StringUtils.isNotBlank(name)) {
			userReset(name);
		}
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public void userReset(String name) {
		// Gets the email from this user
		String email = getEmail(name);
		// Gets the display name for this user
		String displayName = getDisplayName(name);
		// Creates the reset message
		Message message = createResetUserMessage(name, displayName);
		// Sends the message
		messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
	}

	@Override
	@Transactional
	public Ack userForgotten(String email) {
		try {
			// Gets the name for this user
			String name = getNamedParameterJdbcTemplate().queryForObject(SQL.USER_FORGOTTEN, new MapSqlParameterSource(EMAIL, email), String.class);
			// Gets the display name for this user
			String displayName = getDisplayName(name);
			// Creates the message to the user
			Message message = createUserForgottenMessage(name, displayName);
			// Sends the message
			Ack ack = messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
			// OK
			return ack;
		} catch (EmptyResultDataAccessException ex) {
			// Cannot find the mail
			return Ack.NOK;
		}
	}

	private String getEmail(String name) {
		return getNamedParameterJdbcTemplate().queryForObject(SQL.USER_EMAIL, new MapSqlParameterSource(USER, name), String.class);
	}

	private String getDisplayName(String name) {
		return getNamedParameterJdbcTemplate().queryForObject(SQL.USER_DISPLAY_NAME, new MapSqlParameterSource(USER, name), String.class);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public void userDisable(String name) {
		getNamedParameterJdbcTemplate().update(SQL.USER_DISABLE, new MapSqlParameterSource(USER, name));
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public void userEnable(String name) {
		getNamedParameterJdbcTemplate().update(SQL.USER_ENABLE, new MapSqlParameterSource(USER, name));
	}

}
