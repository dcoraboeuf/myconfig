package net.myconfig.service.exception;


public class VersionAlreadyDefinedException extends VersionInputException {

	public VersionAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
