package net.myconfig.service.security.manager;

import java.util.Collections;
import java.util.EnumSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserProfile;
import net.myconfig.service.security.AppFunctionKey;
import net.myconfig.service.security.EnvFunctionKey;
import net.myconfig.service.security.UserProfileImpl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoSecurityManagement extends AbstractSecurityManagement {

	public NoSecurityManagement() {
		super("none");
	}

	@Override
	public UserProfile authenticate(Authentication authentication) {
		return new UserProfileImpl(new User("anonymous", true), EnumSet.noneOf(UserFunction.class), Collections.<AppFunctionKey> emptySet(), Collections.<EnvFunctionKey> emptySet());
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
