package net.myconfig.service.exception;

import net.myconfig.core.CoreException;


public class KeyNotFoundException extends CoreException {

	public KeyNotFoundException(String application, String key) {
		super(application, key);
	}

}
