package net.myconfig.service.api.security;

import net.myconfig.core.UserFunction;

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
			Object details = authentication.getDetails();
			if (details instanceof UserProfile) {
				return (UserProfile) details;
			} else {
				return null;
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

}
