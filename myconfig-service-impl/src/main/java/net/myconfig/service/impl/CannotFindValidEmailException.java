package net.myconfig.service.impl;

import net.myconfig.service.exception.CoreException;

public class CannotFindValidEmailException extends CoreException {

	public CannotFindValidEmailException(String name) {
		super(name);
	}

}
