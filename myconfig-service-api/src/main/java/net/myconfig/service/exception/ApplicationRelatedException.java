package net.myconfig.service.exception;

import net.myconfig.core.InputException;

public abstract class ApplicationRelatedException extends InputException {

	private final String id;

	public ApplicationRelatedException(String id, Object... params) {
		super(params);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
