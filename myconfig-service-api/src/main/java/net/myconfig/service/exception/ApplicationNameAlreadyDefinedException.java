package net.myconfig.service.exception;

import net.myconfig.core.InputException;

public class ApplicationNameAlreadyDefinedException extends InputException {

	public ApplicationNameAlreadyDefinedException(String name) {
		super(name);
	}

}
