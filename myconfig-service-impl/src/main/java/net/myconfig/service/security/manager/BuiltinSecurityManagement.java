package net.myconfig.service.security.manager;

import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuiltinSecurityManagement extends AbstractUserTokenSecurityManagement {

	private final SecurityService securityService;

	@Autowired
	public BuiltinSecurityManagement(SecurityService securityService) {
		super("builtin");
		this.securityService = securityService;
	}

	protected UserToken getUserToken(String username, String password) {
		return securityService.getUserToken (username, password);
	}

}
