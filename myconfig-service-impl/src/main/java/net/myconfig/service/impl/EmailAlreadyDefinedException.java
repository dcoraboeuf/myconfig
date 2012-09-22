package net.myconfig.service.impl;

import net.myconfig.service.exception.InputException;

public class EmailAlreadyDefinedException extends InputException {

	public EmailAlreadyDefinedException(String email) {
		super(email);
	}

}
