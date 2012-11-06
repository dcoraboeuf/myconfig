package net.myconfig.service.impl;

import net.myconfig.core.InputException;

public class CannotChangePasswordException extends InputException {

	public CannotChangePasswordException(String name) {
		super(name);
	}

}
