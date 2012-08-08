package net.myconfig.service.impl;

import static net.myconfig.service.impl.SQLColumns.NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserGrant;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.SecurityManagementNotFoundException;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.myconfig.service.validation.UserValidation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Service
public class SecurityServiceImpl extends AbstractSecurityService implements SecurityService {

	private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	private final ConfigurationService configurationService;
	private final Set<String> securityModes;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, ConfigurationService configurationService, Collection<SecurityManagement> securityManagements) {
		super(dataSource, validator);
		this.configurationService = configurationService;
		this.securityModes = ImmutableSet.copyOf(Iterables.transform(securityManagements, new Function<SecurityManagement, String>() {
			@Override
			public String apply(SecurityManagement input) {
				return input.getId();
			}
		}));
	}
	
	@Override
	@Transactional(readOnly = true)
	public String getSecurityMode() {
		return configurationService.getParameter(ConfigurationService.SECURITY_MODE, ConfigurationService.SECURITY_MODE_DEFAULT);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_setup)
	public void setSecurityMode(String mode) {
		logger.info("[security] Changing security mode to {}", mode);
		// FIXME Security mode - duplicate code
		String currentMode = getSecurityMode();
		if (!StringUtils.equals(currentMode, mode)) {
			if (!securityModes.contains(mode)) {
				throw new SecurityManagementNotFoundException(mode);
			}
			configurationService.setParameter(ConfigurationService.SECURITY_MODE, mode);

		} else {
			logger.info("[security] {} mode is already selected.", mode);
		}
	}
	
	@Override
	public List<String> getSecurityModes() {
		ArrayList<String> modes = new ArrayList<String>(securityModes);
		Collections.sort(modes);
		return modes;
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
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userCreate(String name) {
		validate(UserValidation.class, NAME, name);
		try {
			int count = getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, new MapSqlParameterSource(SQLColumns.NAME, name));
			return Ack.one(count);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException(name);
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
		return Ack.one(count);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionAdd(String name, UserFunction fn) {
		userFunctionRemove(name, fn);
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_ADD, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

	@Override
	@Transactional
	@UserGrant(UserFunction.security_users)
	public Ack userFunctionRemove(String name, UserFunction fn) {
		int count = getNamedParameterJdbcTemplate().update(SQL.FUNCTIONS_USER_REMOVE, new MapSqlParameterSource().addValue(SQLColumns.USER, name).addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
		return Ack.one(count);
	}

}
