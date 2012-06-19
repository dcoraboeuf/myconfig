package net.myconfig.service.exception;


public class EnvironmentAlreadyDefinedException extends ApplicationRelatedException {

	public EnvironmentAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
