package net.myconfig.service.exception;


public class EnvironmentNotFoundException extends CoreException {
	
	public EnvironmentNotFoundException(String application, String name) {
		super (application, name);
	}

}
