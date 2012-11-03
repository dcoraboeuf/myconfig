package net.myconfig.service.exception;

public abstract class EnvironmentInputException extends ApplicationRelatedException {

	public EnvironmentInputException(String id, Object... params) {
		super(id, params);
	}

}
