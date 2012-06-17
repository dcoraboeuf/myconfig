package net.myconfig.service.exception;


public class ApplicationNotFoundException extends CoreException {
	
	public ApplicationNotFoundException(String name) {
		super (name);
	}

}
