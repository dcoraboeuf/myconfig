package net.myconfig.service.impl;

import net.myconfig.service.exception.InputException;

public class CannotResetUserException extends InputException {

	public CannotResetUserException(String name) {
		super(name);
	}

}
