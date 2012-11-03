package net.myconfig.service.exception;


public class KeyNotDefinedException extends KeyInputException {
	
	public KeyNotDefinedException(String application, String name) {
		super (application, name);
	}

}
