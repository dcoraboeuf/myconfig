package net.myconfig.service.exception;

import net.myconfig.core.InputException;

public class ApplicationIDAlreadyDefinedException extends InputException {

	public ApplicationIDAlreadyDefinedException(String id) {
		super(id);
	}

}
