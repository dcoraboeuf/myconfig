package net.myconfig.service.exception;

import net.myconfig.core.CoreException;

public class UserProviderModeNotDefinedException extends CoreException {

	public UserProviderModeNotDefinedException(String mode) {
		super(mode);
	}

}
