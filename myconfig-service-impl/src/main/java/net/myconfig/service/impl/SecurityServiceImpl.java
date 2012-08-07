package net.myconfig.service.impl;

import static net.myconfig.service.impl.SQLColumns.ADMIN;
import static net.myconfig.service.impl.SQLColumns.NAME;
import static net.myconfig.service.impl.SQLColumns.PASSWORD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.myconfig.service.security.UserTokenImpl;
import net.myconfig.service.validation.UserValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractDaoService implements SecurityService {

	public static final String SECURITY_MODE = "security.mode";

	private static final String SECURITY_MODE_DEFAULT = "none";

	public static void main(String[] args) {
		for (String password : args) {
			System.out.format("%s ==> %s%n", password, digest(password));
		}
	}

	private static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}
	
	private final ConfigurationService configurationService;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, ConfigurationService configurationService) {
		super(dataSource, validator);
		this.configurationService = configurationService;
	}
	
	@Override
	@Transactional(readOnly = true)
	public String getSecurityMode() {
		return configurationService.getParameter(SECURITY_MODE, SECURITY_MODE_DEFAULT);
	}
	
	@Override
	@Transactional
	@UserGrant(UserFunction.security_setup)
	public void setSecurityMode(String mode) {
		configurationService.setParameter(SECURITY_MODE, mode);
	}

	@Override
	@Transactional(readOnly = true)
	@UserGrant(UserFunction.security_users)
	public List<UserSummary> getUserList() {
		List<User> users = getJdbcTemplate().query(SQL.USER_SUMMARIES, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString(SQLColumns.NAME), rs.getBoolean(SQLColumns.ADMIN));
			}
		});
		return Lists.transform(users, new Function<User, UserSummary>() {
			@Override
			public UserSummary apply(User user) {
				List<UserFunction> functions = getUserFunctions(user);
				return new UserSummary(user.getName(), user.isAdmin(), functions);
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public UserToken getUserToken(String username, String password) {
		// Gets the user
		User user = getUser(username, password);
		if (user == null) {
			return null;
		}
		// User functions
		List<UserFunction> userFunctions = getUserFunctions(user);
		// Application functions
		Map<Integer, Set<AppFunction>> appFunctions = getAppFunctions(user);
		// OK
		return new UserTokenImpl(user, userFunctions, appFunctions);
	}
	
	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userCreate(String name) {
		validate(UserValidation.class, NAME, name);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, new MapSqlParameterSource(SQLColumns.NAME, name)); 
			return Ack.one (count);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException (name);
		}
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
		return Ack.one (count);
	}
	
	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionAdd(String name, UserFunction fn) {
		userFunctionRemove(name, fn);
		int count = getNamedParameterJdbcTemplate().update(
				SQL.FUNCTIONS_USER_ADD,
				new MapSqlParameterSource()
					.addValue(SQLColumns.USER, name)
					.addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}
	
	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionRemove(String name, UserFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(
				SQL.FUNCTIONS_USER_REMOVE,
				new MapSqlParameterSource()
					.addValue(SQLColumns.USER, name)
					.addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

	protected Map<Integer, Set<AppFunction>> getAppFunctions(User user) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_APP, new MapSqlParameterSource().addValue(SQLColumns.USER, user.getName()));
		Map<Integer, Set<AppFunction>> result = new HashMap<Integer, Set<AppFunction>>();
		for (Map<String, Object> row : list) {
			int application = (Integer) row.get(SQLColumns.APPLICATION);
			AppFunction fn = AppFunction.valueOf((String) row.get(SQLColumns.GRANTEDFUNCTION));
			Set<AppFunction> set = result.get(application);
			if (set == null) {
				set = new HashSet<AppFunction>();
				result.put(application, set);
			}
			set.add(fn);
		}
		return result;
	}

	protected List<UserFunction> getUserFunctions(User user) {
		return Lists.transform(getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_USER, new MapSqlParameterSource(SQLColumns.USER, user.getName()), String.class),
				new Function<String, UserFunction>() {
					@Override
					public UserFunction apply(String name) {
						return UserFunction.valueOf(name);
					}
				});
	}

	protected User getUser(String username, String password) {
		final String digest = digest(password);
		List<User> users = getNamedParameterJdbcTemplate().query(SQL.USER, new MapSqlParameterSource().addValue(NAME, username).addValue(PASSWORD, digest), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int row) throws SQLException {
				return new User(rs.getString(NAME), rs.getBoolean(ADMIN));
			}
		});
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}

}
