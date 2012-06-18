package net.myconfig.service.exception;


public class VersionAlreadyDefinedException extends ApplicationRelatedException {

	public VersionAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
