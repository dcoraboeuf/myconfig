package net.myconfig.service.exception;

import net.myconfig.core.CoreException;


public class ApplicationNotFoundException extends CoreException {
	
	public ApplicationNotFoundException(String idOrName) {
		super (idOrName);
	}

}
