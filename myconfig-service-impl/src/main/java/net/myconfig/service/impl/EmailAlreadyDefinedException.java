package net.myconfig.service.impl;

import net.myconfig.core.InputException;

public class EmailAlreadyDefinedException extends InputException {

	public EmailAlreadyDefinedException(String email) {
		super(email);
	}

}
