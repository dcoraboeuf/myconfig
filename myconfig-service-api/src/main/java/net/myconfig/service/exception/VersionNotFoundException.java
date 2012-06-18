package net.myconfig.service.exception;


public class VersionNotFoundException extends CoreException {
	
	public VersionNotFoundException(String application, String name) {
		super (application, name);
	}

}
