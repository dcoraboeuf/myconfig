package net.myconfig.service.exception;


public class VersionNotDefinedException extends CoreException {
	
	public VersionNotDefinedException(int application, String name) {
		super (application, name);
	}

}
