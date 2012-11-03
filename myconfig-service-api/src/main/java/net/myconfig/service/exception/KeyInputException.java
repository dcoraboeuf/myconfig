package net.myconfig.service.exception;

public abstract class KeyInputException extends ApplicationRelatedException {

	public KeyInputException(String id, Object... params) {
		super(id, params);
	}

}
