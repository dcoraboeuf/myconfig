package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public abstract class AbstractUserSecurityManagement extends AbstractSecurityManagement {

	private final GrantService grantService;

	public AbstractUserSecurityManagement(String id, GrantService grantService) {
		super(id);
		this.grantService = grantService;
	}

	@Override
	public String getCurrentUserName() {
		return SecurityUtils.getCurrentUserName();
	}

	@Override
	public boolean hasOneOfUserFunction(UserFunction... fns) {
		User user = getCurrentProfile();
		if (user != null) {
			if (user.isAdmin()) {
				return true;
			} else {
				for (UserFunction fn : fns) {
					if (grantService.hasUserFunction(user.getName(), fn)) {
						return true;
					}
				}
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isLogged() {
		return getCurrentProfile() != null;
	}

	@Override
	public User getCurrentProfile() {
		return SecurityUtils.getCurrentUser();
	}

	@Override
	public User authenticate(Authentication authentication) {
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

	protected User getUserToken(Authentication authentication) {
		if (authentication == null) {
			return null;
		} else {
			Object details = authentication.getDetails();
			if (details instanceof User) {
				return (User) details;
			} else {
				return null;
			}
		}
	}
	
	@Override
	public boolean isAdmin(Authentication authentication) {
		User user = getUserToken(authentication);
		return user != null && user.isAdmin();
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		User user = getUserToken(authentication);
		return user != null && (user.isAdmin() || grantService.hasUserFunction(user.getName(), fn));
	}

	@Override
	public boolean hasApplicationFunction(Authentication authentication, String application, AppFunction fn) {
		User user = getUserToken(authentication);
		return user != null && (user.isAdmin() || grantService.hasAppFunction(application, user.getName(), fn));
	}

	@Override
	public boolean hasEnvironmentFunction(Authentication authentication, String application, String environment, EnvFunction fn) {
		User user = getUserToken(authentication);
		return user != null && (user.isAdmin() || grantService.hasEnvFunction(application, user.getName(), environment, fn));
	}

	@Override
	public boolean allowLogin() {
		return true;
	}

	protected abstract User getUserToken(String username, String password);

}
