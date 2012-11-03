package net.myconfig.service.impl;

import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Environment;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.User;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class AbstractSecureService extends AbstractDaoService {

	private final SecuritySelector securitySelector;
	protected final GrantService grantService;

	public AbstractSecureService(DataSource dataSource, Validator validator, SecuritySelector securitySelector, GrantService grantService) {
		super(dataSource, validator);
		this.securitySelector = securitySelector;
		this.grantService = grantService;
	}

	protected void checkEnvironmentAccess(String application, String environment, EnvFunction fn) {
		if (!hasEnvironmentAccess(application, environment, fn)) {
			throw new AccessDeniedException(String.format("Function %s is denied for environment %s in application %s", fn, environment, application));
		}
	}

	protected boolean hasUserAccess(UserFunction fn) {
		// Gets the authentication
		Authentication authentication = SecurityUtils.authentication();
		// Check
		return securitySelector.hasUserFunction(authentication, fn);
	}

	protected boolean hasApplicationAccess(String application, AppFunction fn) {
		// Gets the authentication
		Authentication authentication = SecurityUtils.authentication();
		// Check
		return securitySelector.hasApplicationFunction(authentication, application, fn);
	}

	protected boolean hasEnvironmentAccess(String application, String environment, EnvFunction fn) {
		// Gets the authentication
		Authentication authentication = SecurityUtils.authentication();
		// Check
		return securitySelector.hasEnvironmentFunction(authentication, application, environment, fn);
	}

	protected ImmutableList<Environment> filterEnvironments(final String application, List<Environment> environments) {
		return ImmutableList.copyOf(Iterables.filter(environments, new Predicate<Environment>() {
			@Override
			public boolean apply(Environment env) {
				return hasEnvironmentAccess(application, env.getName(), EnvFunction.env_view);
			}
		}));
	}

	protected void grantAppFunction(String application, AppFunction fn) {
		User profile = securitySelector.getCurrentProfile();
		if (profile != null && !profile.isAdmin()) {
			String user = profile.getName();
			// Grant
			grantService.appFunctionAdd(application, user, fn);
		}
	}

	protected void grantEnvFunction(String application, String name, EnvFunction fn) {
		User profile = securitySelector.getCurrentProfile();
		if (profile != null && !profile.isAdmin()) {
			String user = profile.getName();
			// Grant
			grantService.envFunctionAdd(application, user, name, fn);
		}
	}

}
