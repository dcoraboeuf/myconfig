package net.myconfig.service.impl;

import static net.myconfig.service.api.security.SecurityUtils.digest;
import static net.myconfig.service.db.SQL.USER_SUMMARIES;
import static net.myconfig.service.db.SQLColumns.ADMIN;
import static net.myconfig.service.db.SQLColumns.DISABLED;
import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.EMAIL;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.PASSWORD;
import static net.myconfig.service.db.SQLColumns.USER;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.UserSummary;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.AppGrantParam;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.api.security.UserProvider;
import net.myconfig.service.api.security.UserProviderFactory;
import net.myconfig.service.audit.Audit;
import net.myconfig.service.cache.CacheNames;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.security.SecurityManagementNotFoundException;
import net.myconfig.service.validation.UserValidation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractDaoService implements SecurityService {

	private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	private final ConfigurationService configurationService;
	private final SecuritySelector securitySelector;
	private final GrantService grantService;
	private final UserProviderFactory userProviderFactory;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, ConfigurationService configurationService, SecuritySelector securitySelector,
			GrantService grantService, UserProviderFactory userProviderFactory) {
		super(dataSource, validator);
		this.configurationService = configurationService;
		this.securitySelector = securitySelector;
		this.grantService = grantService;
		this.userProviderFactory = userProviderFactory;
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_setup)
	@Caching(evict = {
			@CacheEvict(value = CacheNames.USER_FUNCTION, allEntries = true),
			@CacheEvict(value = CacheNames.APP_FUNCTION, allEntries = true),
			@CacheEvict(value = CacheNames.ENV_FUNCTION, allEntries = true)
	})
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
	public UserSummaries getUserList() {
		List<User> users = getJdbcTemplate().query(USER_SUMMARIES, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString(NAME), rs.getString(DISPLAYNAME), rs.getString(EMAIL), rs.getBoolean(ADMIN), rs.getBoolean(VERIFIED), rs.getBoolean(DISABLED));
			}
		});
		// All users
		users.add(0, new User(USER_ALL, null, null, false, true, true));
		// List
		return new UserSummaries(Lists.transform(users, new Function<User, UserSummary>() {
			@Override
			public UserSummary apply(User user) {
				EnumSet<UserFunction> functions = grantService.getUserFunctions(user.getName());
				return new UserSummary(USER_ALL.equals(user.getName()), user.getName(), user.getDisplayName(), user.getEmail(), user.isAdmin(), user.isVerified(), user.isDisabled(), functions);
			}
		}));
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER, action = EventAction.CREATE, user = "#name", message = "#displayName")
	public Ack userCreate(String mode, String name, String displayName, String email) {
		// TODO Audit of the mode
		// Gets the user provider
		UserProvider userProvider = userProviderFactory.getRequiredProvider(mode);
		// Creation
		return userProvider.create(name, displayName, email);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER, action = EventAction.DELETE, user = "#name", result = "#result.success")
	public Ack userDelete(String name) {
		MapSqlParameterSource param = new MapSqlParameterSource(SQLColumns.NAME, name);
		getNamedParameterJdbcTemplate().update(SQL.USER_FUNCTIONS_DELETE, param);
		getNamedParameterJdbcTemplate().update(SQL.APP_FUNCTIONS_DELETE, param);
		getNamedParameterJdbcTemplate().update(SQL.ENV_FUNCTIONS_DELETE, param);
		int count = getNamedParameterJdbcTemplate().update(SQL.USER_DELETE, param);
		return Ack.one(count);
	}

	@Override
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER_FUNCTION, action = EventAction.CREATE, user = "#name", function = "#fn", result = "#result.success")
	public Ack userFunctionAdd(String name, UserFunction fn) {
		return grantService.userFunctionAdd (name, fn);
	}

	@Override
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER_FUNCTION, action = EventAction.DELETE, user = "#name", function = "#fn", result = "#result.success")
	public Ack userFunctionRemove(String name, UserFunction fn) {
		return grantService.userFunctionRemove (name, fn);
	}
	
	@Override
	@AppGrant(AppFunction.app_users)
	@Audit(category = EventCategory.APP_FUNCTION, action = EventAction.CREATE, application = "#application", user = "#user", function = "#fn", result = "#result.success")
	public Ack appFunctionAdd(@AppGrantParam String application, String user, AppFunction fn) {
		return grantService.appFunctionAdd(application, user, fn);
	}
	
	@Override
	@AppGrant(AppFunction.app_users)
	@Audit(category = EventCategory.APP_FUNCTION, action = EventAction.DELETE, application = "#application", user = "#user", function = "#fn", result = "#result.success")
	public Ack appFunctionRemove(@AppGrantParam String application, String user, AppFunction fn) {
		return grantService.appFunctionRemove (application, user, fn);
	}
	
	@Override
	@Transactional
	@EnvGrant(EnvFunction.env_users)
	@Audit(category = EventCategory.ENV_FUNCTION, action = EventAction.CREATE, application = "#application", environment = "#environment", user = "#user", function = "#fn", result = "#result.success")
	public Ack envFunctionAdd(@AppGrantParam String application, String user, @EnvGrantParam String environment, EnvFunction fn) {
		return grantService.envFunctionAdd(application, user, environment, fn);
	}
	
	@Override
	@Transactional
	@EnvGrant(EnvFunction.env_users)
	@Audit(category = EventCategory.ENV_FUNCTION, action = EventAction.DELETE, application = "#application", environment = "#environment", user = "#user", function = "#fn", result = "#result.success")
	public Ack envFunctionRemove(@AppGrantParam String application, String user, @EnvGrantParam String environment, EnvFunction fn) {
		return grantService.envFunctionAdd(application, user, environment, fn);
	}
	
	@Override
	@Transactional
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, message = "'UPDATE ' + #displayName + ',' + #email")
	public void updateUserData(String password, String displayName, String email) {
		// Validation
		validate(UserValidation.class, DISPLAYNAME, displayName);
		validate(UserValidation.class, EMAIL, email);
		// Gets the current user
		String name = securitySelector.getCurrentUserName();
		if (StringUtils.isNotBlank(name)) {
			try {
				int count = getNamedParameterJdbcTemplate().update(SQL.USER_UPDATE, new MapSqlParameterSource()
					.addValue(NAME, name)
					.addValue(PASSWORD, digest(password))
					.addValue(DISPLAYNAME, displayName)
					.addValue(EMAIL, email));
				if (count != 1) {
					throw new CannotUpdateUserDataException (name);
				}
			} catch (DuplicateKeyException ex) {
				// Duplicate email
				throw new EmailAlreadyDefinedException (email);
			}
		}
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'DISABLED'")
	public void userDisable(String name) {
		getNamedParameterJdbcTemplate().update(SQL.USER_DISABLE, new MapSqlParameterSource(USER, name));
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	@Audit(category = EventCategory.USER, action = EventAction.UPDATE, user = "#name", message = "'ENABLED'")
	public void userEnable(String name) {
		getNamedParameterJdbcTemplate().update(SQL.USER_ENABLE, new MapSqlParameterSource(USER, name));
	}

}
