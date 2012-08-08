package net.myconfig.service.security.manager;

import java.util.Collections;
import java.util.Set;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.security.UserTokenImpl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoSecurityManagement extends AbstractSecurityManagement {

	public NoSecurityManagement() {
		super("none");
	}

	@Override
	public UserToken authenticate(Authentication authentication) {
		return new UserTokenImpl(new User("anonymous", true), Collections.<UserFunction> emptyList(), Collections.<Integer, Set<AppFunction>> emptyMap());
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
