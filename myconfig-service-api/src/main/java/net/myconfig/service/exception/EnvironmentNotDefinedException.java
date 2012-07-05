package net.myconfig.service.exception;


public class EnvironmentNotDefinedException extends EnvironmentInputException {
	
	public EnvironmentNotDefinedException(int application, String name) {
		super (application, name);
	}

}
