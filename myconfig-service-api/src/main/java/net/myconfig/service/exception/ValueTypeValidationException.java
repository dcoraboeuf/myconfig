package net.myconfig.service.exception;

import net.sf.jstring.LocalizableException;

public class ValueTypeValidationException extends LocalizableException {

	public ValueTypeValidationException(String key, String value, String param) {
		super(key, value, param);
	}

}
