package net.myconfig.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.dbinit.DBInitAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

@Service
public class InitSecurity implements DBInitAction {

	private final Logger logger = LoggerFactory.getLogger(InitSecurity.class);

	/**
	 * Detects if a default 'admin' user must be created.
	 */
	@Override
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

}
