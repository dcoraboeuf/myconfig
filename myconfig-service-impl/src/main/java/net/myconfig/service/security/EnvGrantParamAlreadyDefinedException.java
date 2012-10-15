package net.myconfig.service.security;

import net.myconfig.core.CoreException;

public class EnvGrantParamAlreadyDefinedException extends CoreException {

	public EnvGrantParamAlreadyDefinedException(String name) {
		super(name);
	}

}
