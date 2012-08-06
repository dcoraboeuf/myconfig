package net.myconfig.service.security;

import static net.myconfig.service.impl.SQLColumns.ADMIN;
import static net.myconfig.service.impl.SQLColumns.NAME;
import static net.myconfig.service.impl.SQLColumns.PASSWORD;

import java.sql.Connection;
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
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.impl.AbstractDaoService;
import net.myconfig.service.impl.SQL;
import net.myconfig.service.impl.SQLColumns;
import net.myconfig.service.model.UserSummary;
import net.sf.dbinit.DBInitAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractDaoService implements SecurityService, DBInitAction {

	public static void main(String[] args) {
		for (String password : args) {
			System.out.format("%s ==> %s%n", password, digest(password));
		}
	}

	private static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}

	private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	/**
	 * Detects if a default 'admin' user must be created.
	 */
	public void run(Connection connection) throws SQLException {
		JdbcTemplate t = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
		logger.info("[security] [init] Initializing the security service...");
		int userCount = t.queryForInt(SQL.USER_COUNT);
		logger.info("[security] [init] Number of users: {}", userCount);
		if (userCount > 0) {
			logger.info("[security] [init] Some users exist - no need to create any user");
		} else {
			logger.info("[security] [init] No user exists - needs to create default 'admin' user");
			t.execute(SQL.USER_INIT);
			logger.info("[security] [init] Default 'admin' user has been created.");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserSummary> getUserList() {
		return getJdbcTemplate().query(SQL.USER_SUMMARIES, new RowMapper<UserSummary>() {
			@Override
			public UserSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserSummary(rs.getString(SQLColumns.NAME), rs.getBoolean(SQLColumns.ADMIN));
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
