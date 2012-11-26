package net.myconfig.service.security.provider;

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

import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.User;
import net.myconfig.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BuiltinUserProvider extends AbstractUserProvider {

	@Autowired
	public BuiltinUserProvider(DataSource dataSource, Validator validator) {
		super(dataSource, validator, "builtin");
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

}
