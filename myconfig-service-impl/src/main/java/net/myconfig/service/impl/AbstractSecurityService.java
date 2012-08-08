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
import net.myconfig.service.api.security.User;

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