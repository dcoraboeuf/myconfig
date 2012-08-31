package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static Authentication authentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication();
	}

	public static UserProfile profile() {
		Authentication authentication = authentication();
		if (authentication != null) {
			if (authentication instanceof AnonymousAuthenticationToken) {
				return anonymousProfile();
			} else {
				Object details = authentication.getDetails();
				if (details instanceof UserProfile) {
					return (UserProfile) details;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	public static boolean hasOneOfUserFunction(UserFunction... fns) {
		UserProfile profile = profile();
		if (profile != null) {
			for (UserFunction fn : fns) {
				if (profile.hasUserFunction(fn)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

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
				return "anonymous";
			}
			
			@Override
			public String getDisplayName() {
				return "Anonymous";
			}
		};
	}

}
