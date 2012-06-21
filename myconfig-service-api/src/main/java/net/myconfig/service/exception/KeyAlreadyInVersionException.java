package net.myconfig.service.exception;

public class KeyAlreadyInVersionException extends InputException {
	
	public KeyAlreadyInVersionException(String version, String key) {
		super (version, key);
	}

}
