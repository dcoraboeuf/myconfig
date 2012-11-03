package net.myconfig.service.security;

import net.myconfig.core.CoreException;

public class AppGrantParamMissingException extends CoreException {

	public AppGrantParamMissingException(String name) {
		super(name);
	}

}
