package net.myconfig.service.security;

import net.myconfig.service.api.security.UserToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class HubAuthProvider extends AbstractHubSelectorDependency implements AuthenticationProvider {
	
	@Autowired
	public HubAuthProvider(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserToken userToken = getSecuritySelector().authenticate(authentication);
		if (userToken != null) {
			return new UserAuthenticationToken(userToken, authentication);
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return getSecuritySelector().supports(authentication);
	}

}
