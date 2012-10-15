package net.myconfig.service.exception;

import net.myconfig.core.CoreException;

public abstract class InputException extends CoreException {

	public InputException(Object... params) {
		super(params);
	}

}
