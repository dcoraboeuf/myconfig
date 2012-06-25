package net.myconfig.service.exception;

import net.sf.jstring.Localizable;

public class ValidationException extends InputException {

	public ValidationException(Localizable message) {
		super(message);
	}

}
