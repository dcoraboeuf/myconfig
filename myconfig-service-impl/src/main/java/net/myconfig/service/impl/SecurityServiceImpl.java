package net.myconfig.service.impl;

import static net.myconfig.service.impl.SQLColumns.ADMIN;
import static net.myconfig.service.impl.SQLColumns.NAME;
import static net.myconfig.service.impl.SQLColumns.PASSWORD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.MyConfigFunctions;
import net.myconfig.service.security.SecurityService;
import net.myconfig.service.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractDaoService implements SecurityService {
	
	public static void main(String[] args) {
		for (String password : args) {
			System.out.format("%s ==> %s%n", password, digest(password));
		}
	}
	
	private static String digest (String input) {
		return Sha512DigestUtils.shaHex(input);
	}

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyConfigFunctions> getUserFunctions(String username) {
		return Lists.transform(
				getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_USER, new MapSqlParameterSource(SQLColumns.USER, username), String.class),
				new Function<String, MyConfigFunctions>() {
					@Override
					public MyConfigFunctions apply(String name) {
						return MyConfigFunctions.valueOf(name);
					}
				});
	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(String username, String password) {
		List<User> users = getNamedParameterJdbcTemplate().query(SQL.USER,
				new MapSqlParameterSource()
					.addValue(NAME, username)
					.addValue(PASSWORD, digest(password)),
				new RowMapper<User>() {
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
