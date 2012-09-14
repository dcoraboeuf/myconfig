package net.myconfig.service.impl;

import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.UserProfile;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.model.Environment;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class AbstractSecureService extends AbstractDaoService {

	private final SecuritySelector securitySelector;

	public AbstractSecureService(DataSource dataSource, Validator validator, SecuritySelector securitySelector) {
		super(dataSource, validator);
		this.securitySelector = securitySelector;
	}

	protected void checkEnvironmentAccess(int application, String environment, EnvFunction fn) {
		if (!hasEnvironmentAccess(application, environment, fn)) {
			throw new AccessDeniedException(String.format("Function %s is denied for environment %s in application %s", fn, environment, application));
		}
	}

	protected boolean hasEnvironmentAccess(int application, String environment, EnvFunction fn) {
		// Gets the authentication
		Authentication authentication = SecurityUtils.authentication();
		// Check
		return securitySelector.hasEnvironmentFunction(authentication, application, environment, fn);
	}

	protected ImmutableList<Environment> filterEnvironments(final int application, List<Environment> environments) {
		return ImmutableList.copyOf(Iterables.filter(environments, new Predicate<Environment>() {
			@Override
			public boolean apply(Environment env) {
				return hasEnvironmentAccess(application, env.getName(), EnvFunction.env_view);
			}
		}));
	}

	protected void grantAppFunction(int application, AppFunction fn) {
		UserProfile profile = securitySelector.getCurrentProfile();
		if (profile != null && !profile.isAdmin()) {
			String user = profile.getName();
			// Grant
			getNamedParameterJdbcTemplate().update(
					SQL.GRANT_APP_FUNCTION,
					new MapSqlParameterSource()
						.addValue(SQLColumns.USER, user)
						.addValue(SQLColumns.APPLICATION, application)
						.addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
			// FIXME Updates the current user profile
		}
	}

	protected void grantEnvFunction(int application, String name, EnvFunction fn) {
		UserProfile profile = securitySelector.getCurrentProfile();
		if (profile != null && !profile.isAdmin()) {
			String user = profile.getName();
			// Grant
			getNamedParameterJdbcTemplate().update(
					SQL.GRANT_ENV_FUNCTION,
					new MapSqlParameterSource()
						.addValue(SQLColumns.USER, user)
						.addValue(SQLColumns.APPLICATION, application)
						.addValue(SQLColumns.ENVIRONMENT, name)
						.addValue(SQLColumns.GRANTEDFUNCTION, fn.name()));
			// FIXME Updates the current user profile
		}
	}

}
