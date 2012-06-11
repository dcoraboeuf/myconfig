package net.myconfig.service.impl;

import net.myconfig.core.CoreException;

public class ApplicationNameAlreadyDefinedException extends CoreException {

	public ApplicationNameAlreadyDefinedException(String name) {
		super (name);
	}

}
