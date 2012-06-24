package net.myconfig.service.exception;


public class VersionNotDefinedException extends VersionInputException {
	
	public VersionNotDefinedException(int application, String name) {
		super (application, name);
	}

}
