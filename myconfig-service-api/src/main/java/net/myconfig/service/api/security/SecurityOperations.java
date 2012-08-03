package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;

import org.springframework.security.core.Authentication;


public interface SecurityOperations {

	UserToken authenticate(Authentication authentication);

	boolean supports(Class<?> authentication);

	boolean hasUserFunction(Authentication authentication, UserFunction fn);

	boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn);

	boolean allowLogin();

}
