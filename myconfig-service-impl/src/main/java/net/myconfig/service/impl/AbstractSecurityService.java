package net.myconfig.service.impl;

import static net.myconfig.service.db.SQLColumns.ADMIN;
import static net.myconfig.service.db.SQLColumns.DISABLED;
import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.EMAIL;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.PASSWORD;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.security.User;
import net.myconfig.service.db.SQL;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;

public abstract class AbstractSecurityService extends AbstractDaoService {

	protected static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}

	public AbstractSecurityService(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	protected User getUser(String username, String password) {
		final String digest = digest(password);
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

}
