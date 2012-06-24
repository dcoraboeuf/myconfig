package net.myconfig.service.exception;

public abstract class EnvironmentInputException extends ApplicationRelatedException {

	public EnvironmentInputException(int id, Object... params) {
		super(id, params);
	}

}
