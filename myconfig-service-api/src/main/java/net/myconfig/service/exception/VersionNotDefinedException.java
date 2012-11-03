package net.myconfig.service.exception;


public class VersionNotDefinedException extends VersionInputException {
	
	public VersionNotDefinedException(String application, String name) {
		super (application, name);
	}

}
