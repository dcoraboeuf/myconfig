package net.myconfig.service.impl;

import net.myconfig.core.CoreException;

public class VersionAlreadyDefinedException extends CoreException {

	public VersionAlreadyDefinedException(String name) {
		super (name);
	}

}
