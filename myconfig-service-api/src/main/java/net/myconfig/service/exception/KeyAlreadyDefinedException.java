package net.myconfig.service.exception;


public class KeyAlreadyDefinedException extends ApplicationRelatedException {

	public KeyAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
