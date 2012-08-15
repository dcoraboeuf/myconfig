package net.myconfig.service.impl;

import static net.myconfig.service.impl.SQL.USER_SUMMARIES;
import static net.myconfig.service.impl.SQLColumns.ADMIN;
import static net.myconfig.service.impl.SQLColumns.NAME;
import static net.myconfig.service.impl.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.UserFunction;
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
import net.myconfig.service.model.Ack;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractSecurityService implements SecurityService {

	// TODO Use templating
	private static final String NEW_USER_MESSAGE = "Dear %1$s,%n%n" +
			"A new account '%1$s' has been registered for you.%n%n" +
			"Please follow this link in order to validate your account and " +
			"to create your password.%n%n" +
			"%2$s%n%n" +
			"Regards,%n" +
			"the myconfig team.";

	private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	private final ConfigurationService configurationService;
	private final SecuritySelector securitySelector;
	private final MessageService messageService;
	private final UIService uiService;
	private final TokenService tokenService;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, ConfigurationService configurationService, SecuritySelector securitySelector, MessageService messageService, UIService uiService, TokenService tokenService) {
		super(dataSource, validator);
		this.configurationService = configurationService;
		this.securitySelector = securitySelector;
		this.messageService = messageService;
		this.uiService = uiService;
		this.tokenService = tokenService;
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
			configurationService.setParameter(ConfigurationService.SECURITY_MODE, mode);

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
				return new User(rs.getString(NAME), rs.getBoolean(ADMIN), rs.getBoolean(VERIFIED));
			}
		});
		return Lists.transform(users, new Function<User, UserSummary>() {
			@Override
			public UserSummary apply(User user) {
				EnumSet<UserFunction> functions = getUserFunctions(user);
				return new UserSummary(user.getName(), user.isAdmin(), user.isVerified(), functions);
			}
		});
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userCreate(String name, String email) {
		validate(UserValidation.class, NAME, name);
		try {
			// Creates the user
			int count = getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, new MapSqlParameterSource()
				.addValue(SQLColumns.NAME, name)
				.addValue(SQLColumns.EMAIL, email));
			// Its initial state is not verified and a notification must be sent to the email
			Message message = createNewUserMessage (name);
			// Sends the message
			Ack ack = messageService.sendMessage (message, new MessageDestination (MessageChannel.EMAIL, email));
			// OK
			return Ack.one(count).and(ack);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException(name);
		}
	}

	private Message createNewUserMessage(String name) {
		// Generates a token for the response
		String token = tokenService.generateToken(TokenService.TokenType.NEW_USER, name);
		// Gets the return link
		String link = uiService.getLink(UIService.Link.NEW_USER, name, token);
		// Creates the message
		return new Message(
				String.format("myconfig - registration for account", name),
				String.format(NEW_USER_MESSAGE, name, link));
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

}
