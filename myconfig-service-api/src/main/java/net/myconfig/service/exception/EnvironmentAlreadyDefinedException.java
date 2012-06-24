package net.myconfig.service.exception;


public class EnvironmentAlreadyDefinedException extends EnvironmentInputException {

	public EnvironmentAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
