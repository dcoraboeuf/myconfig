package net.myconfig.service.exception;

import net.myconfig.core.CoreException;

public class MatrixNotFoundException extends CoreException {

	public MatrixNotFoundException(String application, String version, String key) {
		super(application, version, key);
	}

}
