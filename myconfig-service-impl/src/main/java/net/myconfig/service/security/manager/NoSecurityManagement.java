package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserProfile;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoSecurityManagement extends AbstractSecurityManagement {

	private static final String ANONYMOUS_USER_NAME = "anonymous";

	public static UserProfile anonymousProfile() {
		return new UserProfile() {

			@Override
			public boolean isAdmin() {
				return true;
			}

			@Override
			public boolean hasUserFunction(UserFunction fn) {
				return true;
			}

			@Override
			public boolean hasEnvFunction(int application, String environment, EnvFunction fn) {
				return true;
			}

			@Override
			public boolean hasAppFunction(int application, AppFunction fn) {
				return true;
			}

			@Override
			public String getName() {
				return ANONYMOUS_USER_NAME;
			}

			@Override
			public String getDisplayName() {
				return "Anonymous";
			}
			
			@Override
			public String getEmail() {
				return null;
			}
		};
	}

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
	public UserProfile authenticate(Authentication authentication) {
		return anonymousProfile();
	}
	
	@Override
	public UserProfile getCurrentProfile() {
		return anonymousProfile();
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
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		return true;
	}

	@Override
	public boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn) {
		return true;
	}

	@Override
	public boolean hasEnvironmentFunction(Authentication authentication, int application, String environment, EnvFunction fn) {
		return true;
	}
}
