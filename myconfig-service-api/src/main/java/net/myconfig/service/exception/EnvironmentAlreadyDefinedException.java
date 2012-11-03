package net.myconfig.service.exception;


public class EnvironmentAlreadyDefinedException extends EnvironmentInputException {

	public EnvironmentAlreadyDefinedException(String id, String name) {
		super (id, name);
	}

}
