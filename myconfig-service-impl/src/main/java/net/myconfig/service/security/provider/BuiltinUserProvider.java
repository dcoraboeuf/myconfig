package net.myconfig.service.security.provider;

import static net.myconfig.service.api.security.SecurityUtils.digest;
import static net.myconfig.service.db.SQLColumns.ADMIN;
import static net.myconfig.service.db.SQLColumns.DISABLED;
import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.EMAIL;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.MODE;
import static net.myconfig.service.db.SQLColumns.NEWPASSWORD;
import static net.myconfig.service.db.SQLColumns.PASSWORD;
import static net.myconfig.service.db.SQLColumns.USER;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.Message;
import net.myconfig.core.model.MessageContent;
import net.myconfig.core.model.TokenType;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.UIService;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.api.message.MessageDestination;
import net.myconfig.service.api.message.MessageService;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.api.security.UserManager;
import net.myconfig.service.api.template.TemplateModel;
import net.myconfig.service.api.template.TemplateService;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.db.SQL;
import net.myconfig.service.impl.CannotChangePasswordException;
import net.myconfig.service.impl.CannotFindValidEmailException;
import net.myconfig.service.impl.CannotResetUserException;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.myconfig.service.token.TokenService;
import net.myconfig.service.validation.UserValidation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BuiltinUserProvider extends AbstractUserProvider implements UserManager {

	private static final String USER_CREATE = "insert into users (mode, name, displayname, password, admin, email, verified, disabled) values (:mode, :name, :displayName, '', false, :email, false, false)";

	private final UIService uiService;
	private final TokenService tokenService;
	private final TemplateService templateService;
	private final ConfigurationService configurationService;
	private final MessageService messageService;

	@Autowired
	public BuiltinUserProvider(DataSource dataSource, Validator validator, UIService uiService, TokenService tokenService, TemplateService templateService,
			ConfigurationService configurationService, MessageService messageService) {
		super(dataSource, validator, "builtin");
		this.uiService = uiService;
		this.tokenService = tokenService;
		this.templateService = templateService;
		this.configurationService = configurationService;
		this.messageService = messageService;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(String username, String password) {
		final String digest = SecurityUtils.digest(password);
		List<User> users = getNamedParameterJdbcTemplate().query(SQL.USER, new MapSqlParameterSource().addValue(NAME, username).addValue(PASSWORD, digest), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int row) throws SQLException {
				return new User(rs.getString(NAME), rs.getString(DISPLAYNAME), rs.getString(EMAIL), rs.getBoolean(ADMIN), rs.getBoolean(VERIFIED), rs.getBoolean(DISABLED));
			}
		});
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Ack create(String name, String displayName, String email) {
		// Validation
		validate(UserValidation.class, NAME, name);
		validate(UserValidation.class, DISPLAYNAME, displayName);
		validate(UserValidation.class, EMAIL, email);
		try {
			// Creates the user
			int count = getNamedParameterJdbcTemplate().update(USER_CREATE, new MapSqlParameterSource(MODE, getId()).addValue(NAME, name).addValue(DISPLAYNAME, displayName).addValue(EMAIL, email));
			// Its initial state is not verified and a notification must be sent
			// to the email
			Message message = createNewUserMessage(name, displayName);
			// Sends the message
			Ack ack = messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
			// OK
			return Ack.one(count).and(ack);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException(name, email);
		}
	}

	@Override
	public void checkUserConfirm(String name, String token) {
		tokenService.checkToken(token, TokenType.NEW_USER, name);
	}

	@Override
	public void checkUserForgotten(String name, String token) {
		tokenService.checkToken(token, TokenType.FORGOTTEN_PASSWORD, name);
	}

	@Override
	public void checkUserReset(String name, String token) {
		tokenService.checkToken(token, TokenType.RESET_USER, name);
	}

	@Override
	public void checkUserChangePassword(String name, String token) {
		tokenService.checkToken(token, TokenType.USER_CHANGE_PASSWORD, name);
	}

	@Override
	@Transactional
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'RESET'")
	public void userReset(String name, String token, String password) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.RESET_USER, name);
		// Changes the password
		int count = getNamedParameterJdbcTemplate().update(SQL.USER_RESET, new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(password)));
		// Check
		if (count != 1) {
			throw new CannotResetUserException(name);
		}
	}

	@Override
	@Transactional
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'PASSWORD'")
	public void userChangePassword(String name, String token, String oldPassword, String newPassword) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.USER_CHANGE_PASSWORD, name);
		// Changes the password
		int count = getNamedParameterJdbcTemplate().update(SQL.USER_CHANGE_PASSWORD,
				new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(oldPassword)).addValue(NEWPASSWORD, digest(newPassword)));
		// Check
		if (count != 1) {
			throw new CannotChangePasswordException(name);
		}
	}

	@Override
	@Transactional
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'CONFIRM'")
	public void userConfirm(String name, String token, String password) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.NEW_USER, name);
		// Saves the password
		getNamedParameterJdbcTemplate().update(SQL.USER_CONFIRM, new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(password)));
	}

	@Override
	@Transactional
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'FORGOTTEN'")
	public void userForgottenSet(String name, String token, String password) {
		// Consumes the token
		tokenService.consumesToken(token, TokenType.FORGOTTEN_PASSWORD, name);
		// Saves the password
		getNamedParameterJdbcTemplate().update(SQL.USER_RESET, new MapSqlParameterSource().addValue(USER, name).addValue(PASSWORD, digest(password)));
	}

	@Override
	@Transactional
	public void userChangePassword() {
		String name = SecurityUtils.getCurrentUserName();
		if (StringUtils.isNotBlank(name)) {
			// Gets the email from this user
			String email = getEmail(name);
			// Gets the display name for this user
			String displayName = getDisplayName(name);
			// Creates the reset message
			Message message = createChangePasswordUserMessage(name, displayName);
			// Sends the message
			messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
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
		String email = getNamedParameterJdbcTemplate().queryForObject(SQL.USER_EMAIL, new MapSqlParameterSource(USER, name), String.class);
		if (StringUtils.isBlank(email)) {
			throw new CannotFindValidEmailException(name);
		} else {
			return email;
		}
	}

	private String getDisplayName(String name) {
		return getNamedParameterJdbcTemplate().queryForObject(SQL.USER_DISPLAY_NAME, new MapSqlParameterSource(USER, name), String.class);
	}

	private Message createUserMessage(String user, String userDisplayName, TokenType tokenType, UIService.Link linkType, String templateId, String subjectFormat) {
		// Generates a token for the response
		String token = tokenService.generateToken(tokenType, user);
		// Gets the return link
		String link = uiService.getLink(linkType, user, token);
		// Gets the signature
		String signature = configurationService.getParameter(ConfigurationKey.APP_REPLYTO_NAME);
		// Message template model
		TemplateModel model = new TemplateModel();
		model.add("user", user);
		model.add("userFullName", userDisplayName);
		model.add("link", link);
		model.add("signature", signature);
		// Message content
		String content = templateService.generate(templateId, model);
		// Creates the message
		return new Message(String.format(subjectFormat, user), new MessageContent(content, link, token));
	}

	private String getMessageTitle(String message) {
		String appName = configurationService.getParameter(ConfigurationKey.APP_NAME);
		return String.format("%s - %s", appName, message);
	}

	private Message createNewUserMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.NEW_USER, UIService.Link.NEW_USER, "user_new.txt", getMessageTitle("registration for account"));
	}

	private Message createUserForgottenMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.FORGOTTEN_PASSWORD, UIService.Link.FORGOTTEN_PASSWORD, "user_forgotten.txt", getMessageTitle("account reset"));
	}

	private Message createResetUserMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.RESET_USER, UIService.Link.RESET_USER, "user_reset.txt", getMessageTitle("reset password for account"));
	}

	private Message createChangePasswordUserMessage(String name, String userDisplayName) {
		return createUserMessage(name, userDisplayName, TokenType.USER_CHANGE_PASSWORD, UIService.Link.USER_CHANGE_PASSWORD, "user_change_password.txt", getMessageTitle("change password for account"));
	}

}
