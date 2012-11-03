package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoSecurityManagement extends AbstractSecurityManagement {

	private static final String ANONYMOUS_USER_NAME = "anonymous";

	private final User anonymousProfile = new User(ANONYMOUS_USER_NAME, "Anonymous", "", true, true, false);

	public NoSecurityManagement() {
		super("none");
	}
	
	@Override
	public String getCurrentUserName() {
		return null;
	}
	
	@Override
	public boolean isLogged() {
		return true;
	}
	
	@Override
	public boolean hasOneOfUserFunction(UserFunction... fns) {
		return true;
	}

	@Override
	public User authenticate(Authentication authentication) {
		return anonymousProfile;
	}
	
	@Override
	public User getCurrentProfile() {
		return anonymousProfile;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	@Override
	public boolean allowLogin() {
		return false;
	}
	
	@Override
	public boolean isAdmin(Authentication authentication) {
		return true;
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		return true;
	}

	@Override
	public boolean hasApplicationFunction(Authentication authentication, String application, AppFunction fn) {
		return true;
	}

	@Override
	public boolean hasEnvironmentFunction(Authentication authentication, String application, String environment, EnvFunction fn) {
		return true;
	}
}
