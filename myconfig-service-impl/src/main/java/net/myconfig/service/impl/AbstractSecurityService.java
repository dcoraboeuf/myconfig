package net.myconfig.service.impl;

import static net.myconfig.service.db.SQLColumns.ADMIN;
import static net.myconfig.service.db.SQLColumns.DISABLED;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.PASSWORD;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.security.AppFunctionKey;
import net.myconfig.service.security.EnvFunctionKey;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class AbstractSecurityService extends AbstractDaoService {

	protected static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}

	public AbstractSecurityService(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	protected Set<EnvFunctionKey> getEnvFunctions(User user) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_ENV, new MapSqlParameterSource().addValue(SQLColumns.USER, user.getName()));
		Set<EnvFunctionKey> result = new HashSet<EnvFunctionKey>();
		for (Map<String, Object> row : list) {
			int application = (Integer) row.get(SQLColumns.APPLICATION);
			String environment = (String) row.get(SQLColumns.ENVIRONMENT);
			EnvFunction fn = EnvFunction.valueOf((String) row.get(SQLColumns.GRANTEDFUNCTION));
			result.add(new EnvFunctionKey(application, environment, fn));
		}
		return result;
	}

	protected Set<AppFunctionKey> getAppFunctions(User user) {
		List<Map<String, Object>> list = getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_APP, new MapSqlParameterSource().addValue(SQLColumns.USER, user.getName()));
		Set<AppFunctionKey> result = new HashSet<AppFunctionKey>();
		for (Map<String, Object> row : list) {
			int application = (Integer) row.get(SQLColumns.APPLICATION);
			AppFunction fn = AppFunction.valueOf((String) row.get(SQLColumns.GRANTEDFUNCTION));
			result.add(new AppFunctionKey(application, fn));
		}
		return result;
	}

	protected EnumSet<UserFunction> getUserFunctions(User user) {
		List<UserFunction> fns = Lists.transform(getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_USER, new MapSqlParameterSource(SQLColumns.USER, user.getName()), String.class),
				new Function<String, UserFunction>() {
					@Override
					public UserFunction apply(String name) {
						return UserFunction.valueOf(name);
					}
				});
		if (fns.isEmpty()) {
			return EnumSet.noneOf(UserFunction.class);
		} else {
			return EnumSet.copyOf(fns);
		}
	}

	protected User getUser(String username, String password) {
		final String digest = digest(password);
		List<User> users = getNamedParameterJdbcTemplate().query(SQL.USER, new MapSqlParameterSource().addValue(NAME, username).addValue(PASSWORD, digest), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int row) throws SQLException {
				return new User(rs.getString(NAME), rs.getBoolean(ADMIN), rs.getBoolean(VERIFIED), rs.getBoolean(DISABLED));
			}
		});
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}

}
