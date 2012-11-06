package net.myconfig.service.api.type;

import net.myconfig.core.InputException;
import net.sf.jstring.Localizable;

public class KeyTypeParameterInvalidException extends InputException {

	public KeyTypeParameterInvalidException(String typeId, String typeParam, Localizable message) {
		super(typeId, typeParam, message);
	}

}
