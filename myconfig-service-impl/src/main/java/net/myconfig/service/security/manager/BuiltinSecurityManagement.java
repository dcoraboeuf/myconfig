package net.myconfig.service.security.manager;

import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuiltinSecurityManagement extends AbstractUserSecurityManagement {

	private final AuthenticationService authenticationService;

	@Autowired
	public BuiltinSecurityManagement(AuthenticationService authenticationService, GrantService grantService) {
		super("builtin", grantService);
		this.authenticationService = authenticationService;
	}

	@Override
	protected User getUserToken(String username, String password) {
		return authenticationService.getUserToken (username, password);
	}

}
