package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;

import org.springframework.security.core.Authentication;


public interface SecurityOperations {

	String getCurrentUserName();

	User getCurrentProfile();

	boolean hasOneOfUserFunction(UserFunction... fns);

	boolean isLogged();

	User authenticate(Authentication authentication);

	boolean supports(Class<?> authentication);

	boolean isAdmin(Authentication authentication);

	boolean hasUserFunction(Authentication authentication, UserFunction fn);

	boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn);

	boolean hasEnvironmentFunction(Authentication authentication, int application, String environment, EnvFunction fn);

	boolean allowLogin();

}
