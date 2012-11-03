package net.myconfig.service.exception;

public abstract class VersionInputException extends ApplicationRelatedException {

	public VersionInputException(String id, Object... params) {
		super(id, params);
	}

}
