package net.myconfig.service.exception;

public class ApplicationIDAlreadyDefinedException extends InputException {

	public ApplicationIDAlreadyDefinedException(String id) {
		super(id);
	}

}
