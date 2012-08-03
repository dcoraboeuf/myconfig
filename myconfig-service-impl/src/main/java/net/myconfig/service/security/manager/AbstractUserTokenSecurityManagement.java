package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserToken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public abstract class AbstractUserTokenSecurityManagement extends AbstractSecurityManagement {

	public AbstractUserTokenSecurityManagement(String id) {
		super(id);
	}

	@Override
	public UserToken authenticate(Authentication authentication) {
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
	
	protected UserToken getUserToken (Authentication authentication) {
		if (authentication == null) {
			return null;
		} else {
			Object details = authentication.getDetails();
			if (details instanceof UserToken) {
				return (UserToken) details;
			} else {
				return null;
			}
		}
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		UserToken token = getUserToken(authentication);
		return token != null && token.hasUserFunction(fn);
	}
	
	@Override
	public boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn) {
		UserToken token = getUserToken(authentication);
		return token != null && token.hasAppFunction(application, fn);
	}
	
	@Override
	public boolean allowLogin() {
		return true;
	}

	protected abstract UserToken getUserToken(String username, String password);

}
