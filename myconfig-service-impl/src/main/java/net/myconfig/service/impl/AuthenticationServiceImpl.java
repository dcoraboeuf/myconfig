package net.myconfig.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.security.provider.UserProvider;
import net.myconfig.service.security.provider.UserProviderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl extends AbstractDaoService implements AuthenticationService {
	
	private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private final UserProviderFactory userProviderFactory;

	@Autowired
	public AuthenticationServiceImpl(DataSource dataSource, Validator validator, UserProviderFactory userProviderFactory) {
		super(dataSource, validator);
		this.userProviderFactory = userProviderFactory;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserToken(String username, String password) {
		// Gets the mode
		String mode = getFirstItem(SQL.USER_MODE, new MapSqlParameterSource(SQLColumns.NAME, username), String.class);
		if (mode == null) {
			return null;
		}
		// Gets the user provider
		UserProvider userProvider = userProviderFactory.getProvider(mode);
		if (userProvider == null) {
			logger.error("[authentication] User provider not defined for user {}: {}", username, mode);
			return null;
		}
		// Gets the user definition
		User user = userProvider.getUser (username, password);
		// OK
		return user;
	}

}
