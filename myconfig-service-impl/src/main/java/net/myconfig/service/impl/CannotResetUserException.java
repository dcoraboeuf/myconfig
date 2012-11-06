package net.myconfig.service.impl;

import net.myconfig.core.InputException;

public class CannotResetUserException extends InputException {

	public CannotResetUserException(String name) {
		super(name);
	}

}
