package net.myconfig.service.security;

import net.myconfig.service.api.security.UserToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuiltinAuthenticationProvider extends AbstractUserTokenAuthenticationProvider {

	private final SecurityService securityService;

	@Autowired
	public BuiltinAuthenticationProvider(SecurityService securityService) {
		super("builtin");
		this.securityService = securityService;
	}

	@Override
	protected UserToken getUserToken(String username, String password) {
		return securityService.getUserToken (username, password);
	}

}
