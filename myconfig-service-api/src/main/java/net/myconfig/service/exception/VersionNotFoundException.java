package net.myconfig.service.exception;

import net.myconfig.core.CoreException;


public class VersionNotFoundException extends CoreException {
	
	public VersionNotFoundException(String application, String name) {
		super (application, name);
	}

}
