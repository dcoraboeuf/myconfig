package net.myconfig.service.exception;


public class KeyNotDefinedException extends CoreException {
	
	public KeyNotDefinedException(int application, String name) {
		super (application, name);
	}

}
