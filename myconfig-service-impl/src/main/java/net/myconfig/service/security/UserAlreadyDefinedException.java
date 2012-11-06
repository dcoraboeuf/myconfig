package net.myconfig.service.security;

import net.myconfig.core.InputException;

public class UserAlreadyDefinedException extends InputException {

	public UserAlreadyDefinedException(String name, String email) {
		super(name, email);
	}

}
