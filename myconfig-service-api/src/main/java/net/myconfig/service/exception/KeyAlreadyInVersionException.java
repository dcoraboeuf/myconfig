package net.myconfig.service.exception;

public class KeyAlreadyInVersionException extends KeyInputException {
	
	public KeyAlreadyInVersionException(int id, String version, String key) {
		super (id, version, key);
	}

}
