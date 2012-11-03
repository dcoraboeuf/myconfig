package net.myconfig.service.exception;


public class KeyAlreadyDefinedException extends KeyInputException {

	public KeyAlreadyDefinedException(String id, String name) {
		super (id, name);
	}

}
