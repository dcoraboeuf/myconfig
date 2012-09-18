package net.myconfig.service.security;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class HubAuthProvider implements AuthenticationProvider {
	
	private final SecuritySelector securitySelector;
	
	@Autowired
	public HubAuthProvider(SecuritySelector securitySelector) {
		this.securitySelector = securitySelector;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		User user = securitySelector.authenticate(authentication);
		if (user != null) {
			return new UserAuthentication(user, authentication);
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return securitySelector.supports(authentication);
	}

}
