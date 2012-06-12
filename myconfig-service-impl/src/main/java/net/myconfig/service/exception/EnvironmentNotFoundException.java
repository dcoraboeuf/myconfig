package net.myconfig.service.exception;

import net.myconfig.core.CoreException;

public class EnvironmentNotFoundException extends CoreException {
	
	public EnvironmentNotFoundException(String application, String name) {
		super (application, name);
	}

}
