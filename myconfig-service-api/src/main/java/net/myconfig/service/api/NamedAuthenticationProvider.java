package net.myconfig.service.api;

import org.springframework.security.authentication.AuthenticationProvider;

public interface NamedAuthenticationProvider extends AuthenticationProvider {

	String getId();

}
