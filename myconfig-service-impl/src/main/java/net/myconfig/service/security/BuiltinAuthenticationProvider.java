package net.myconfig.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuiltinAuthenticationProvider extends AbstractFunctionAuthenticationProvider {

	@Autowired
	public BuiltinAuthenticationProvider(SecurityService securityService) {
		super("builtin", securityService);
	}

	@Override
	protected User getUser(String username, String password) {
		return getSecurityService().getUser (username, password);
	}

}
