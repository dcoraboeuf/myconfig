package net.myconfig.service.exception;

public abstract class KeyInputException extends ApplicationRelatedException {

	public KeyInputException(int id, Object... params) {
		super(id, params);
	}

}
