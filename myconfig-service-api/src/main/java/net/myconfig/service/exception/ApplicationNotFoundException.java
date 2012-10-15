package net.myconfig.service.exception;

import net.myconfig.core.CoreException;


public class ApplicationNotFoundException extends CoreException {
	
	public ApplicationNotFoundException(String name) {
		super (name);
	}
	
	public ApplicationNotFoundException(int id) {
		super (id);
	}

}
