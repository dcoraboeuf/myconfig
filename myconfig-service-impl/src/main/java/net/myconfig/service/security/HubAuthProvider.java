package net.myconfig.service.security;

import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class HubAuthProvider implements AuthenticationProvider {

	private final SecuritySelector selector;

	@Autowired
	public HubAuthProvider(SecuritySelector selector) {
		this.selector = selector;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserToken userToken = getSelectedSecurityManagement().authenticate(authentication);
		if (userToken != null) {
			return new UserAuthenticationToken(userToken, authentication);
		} else {
			return null;
		}
	}

	protected SecurityManagement getSelectedSecurityManagement() {
		return selector.getSecurityManagement();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return getSelectedSecurityManagement().supports(authentication);
	}

}
