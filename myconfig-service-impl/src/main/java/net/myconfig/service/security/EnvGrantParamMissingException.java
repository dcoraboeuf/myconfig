package net.myconfig.service.security;

import net.myconfig.core.CoreException;

public class EnvGrantParamMissingException extends CoreException {

	public EnvGrantParamMissingException(String name) {
		super(name);
	}

}
