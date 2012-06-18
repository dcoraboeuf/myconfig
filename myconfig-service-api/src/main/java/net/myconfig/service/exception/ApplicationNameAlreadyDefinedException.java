package net.myconfig.service.exception;

public class ApplicationNameAlreadyDefinedException extends InputException {

	public ApplicationNameAlreadyDefinedException(String name) {
		super(name);
	}

}
