package net.myconfig.service.api.security;

import org.springframework.security.access.AccessDeniedException;
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
	
	public static void checkIsAdmin (SecuritySelector securitySelector) {
		if (securitySelector == null || !securitySelector.isAdmin(SecurityUtils.authentication())) {
			throw new AccessDeniedException("Access restricted to administrators only");
		}
	}

}
