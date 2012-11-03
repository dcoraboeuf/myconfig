package net.myconfig.service.exception;


public class VersionAlreadyDefinedException extends VersionInputException {

	public VersionAlreadyDefinedException(String id, String name) {
		super (id, name);
	}

}
