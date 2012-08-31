package net.myconfig.service.security.manager;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.UserProfile;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoSecurityManagement extends AbstractSecurityManagement {

	public NoSecurityManagement() {
		super("none");
	}

	@Override
	public UserProfile authenticate(Authentication authentication) {
		return SecurityUtils.anonymousProfile();
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
