package net.myconfig.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class DisabledAuthenticationProvider extends AbstractNamedAuthenticationProvider {

	public DisabledAuthenticationProvider() {
		super("disabled");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		authentication.setAuthenticated(true);
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
