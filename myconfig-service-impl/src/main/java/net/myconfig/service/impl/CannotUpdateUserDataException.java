package net.myconfig.service.impl;

import net.myconfig.core.InputException;

public class CannotUpdateUserDataException extends InputException {

	public CannotUpdateUserDataException(String name) {
		super(name);
	}

}
