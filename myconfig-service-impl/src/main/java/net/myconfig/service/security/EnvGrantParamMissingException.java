package net.myconfig.service.security;

import net.myconfig.service.exception.CoreException;

public class EnvGrantParamMissingException extends CoreException {

	public EnvGrantParamMissingException(String name) {
		super(name);
	}

}
