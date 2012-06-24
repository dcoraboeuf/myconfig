package net.myconfig.service.exception;


public class KeyNotDefinedException extends KeyInputException {
	
	public KeyNotDefinedException(int application, String name) {
		super (application, name);
	}

}
