package net.myconfig.service.exception;


public class EnvironmentNotDefinedException extends EnvironmentInputException {
	
	public EnvironmentNotDefinedException(String application, String name) {
		super (application, name);
	}

}
