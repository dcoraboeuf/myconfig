package net.myconfig.service.impl;

import net.myconfig.service.exception.InputException;

public class CannotChangePasswordException extends InputException {

	public CannotChangePasswordException(String name) {
		super(name);
	}

}
