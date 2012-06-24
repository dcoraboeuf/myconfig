package net.myconfig.service.exception;


public class KeyAlreadyDefinedException extends KeyInputException {

	public KeyAlreadyDefinedException(int id, String name) {
		super (id, name);
	}

}
