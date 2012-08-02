package net.myconfig.service.security;

import net.myconfig.service.exception.CoreException;

public class SecurityManagementNotFoundException extends CoreException {

	public SecurityManagementNotFoundException(String mode) {
		super(mode);
	}

}
