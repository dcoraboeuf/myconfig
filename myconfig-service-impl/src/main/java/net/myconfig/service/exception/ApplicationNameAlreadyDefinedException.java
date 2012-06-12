package net.myconfig.service.exception;

import net.myconfig.core.CoreException;

public class ApplicationNameAlreadyDefinedException extends CoreException {

	public ApplicationNameAlreadyDefinedException(String name) {
		super (name);
	}

}
