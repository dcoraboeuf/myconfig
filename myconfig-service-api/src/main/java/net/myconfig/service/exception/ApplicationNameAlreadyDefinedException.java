package net.myconfig.service.exception;


public class ApplicationNameAlreadyDefinedException extends CoreException {

	public ApplicationNameAlreadyDefinedException(String name) {
		super (name);
	}

}
