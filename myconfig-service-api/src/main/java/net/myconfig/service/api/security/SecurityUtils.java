package net.myconfig.service.api.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Sha512DigestUtils;

public final class SecurityUtils {

	private SecurityUtils() {
	}
	
	public static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
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

	public static void main(String[] args) {
		for (String password : args) {
			System.out.format("%s ==> %s%n", password, digest(password));
		}
	}

}
