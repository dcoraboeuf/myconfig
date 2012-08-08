package net.myconfig.service.security.manager;

import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.UserToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuiltinSecurityManagement extends AbstractUserSecurityManagement {

	private final AuthenticationService authenticationService;

	@Autowired
	public BuiltinSecurityManagement(AuthenticationService authenticationService) {
		super("builtin");
		this.authenticationService = authenticationService;
	}

	protected UserToken getUserToken(String username, String password) {
		return authenticationService.getUserToken (username, password);
	}

}
