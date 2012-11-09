package net.myconfig.service.api.type;

import net.myconfig.core.InputException;
import net.sf.jstring.Localizable;

public class KeyValueFormatException extends InputException {

	public KeyValueFormatException(String key, String typeId, String typeParam, String value, Localizable message) {
		super(key, typeId, typeParam, value, message);
	}

}
