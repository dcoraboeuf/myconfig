package net.myconfig.service.security;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserProfile;

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
		UserProfile userToken = securitySelector.authenticate(authentication);
		if (userToken != null) {
			return new UserAuthentication(userToken, authentication);
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return securitySelector.supports(authentication);
	}

}
