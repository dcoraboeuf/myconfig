package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserProfile;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public abstract class AbstractUserSecurityManagement extends AbstractSecurityManagement {

	public AbstractUserSecurityManagement(String id) {
		super(id);
	}

	@Override
	public UserProfile authenticate(Authentication authentication) {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			String username = o.getName();
			String password = (String) authentication.getCredentials();
			// Gets the user
			return getUserToken(username, password);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	protected UserProfile getUserToken (Authentication authentication) {
		if (authentication == null) {
			return null;
		} else {
			Object details = authentication.getDetails();
			if (details instanceof UserProfile) {
				return (UserProfile) details;
			} else {
				return null;
			}
		}
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		UserProfile token = getUserToken(authentication);
		return token != null && token.hasUserFunction(fn);
	}
	
	@Override
	public boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn) {
		UserProfile token = getUserToken(authentication);
		return token != null && token.hasAppFunction(application, fn);
	}
	
	@Override
	public boolean hasEnvironmentFunction(Authentication authentication, int application, String environment, EnvFunction fn) {
		UserProfile token = getUserToken(authentication);
		return token != null && token.hasEnvFunction(application, environment, fn);
	}
	
	@Override
	public boolean allowLogin() {
		return true;
	}

	protected abstract UserProfile getUserToken(String username, String password);

}
