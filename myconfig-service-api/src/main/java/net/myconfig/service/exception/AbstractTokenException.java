package net.myconfig.service.exception;

import net.myconfig.service.model.TokenType;

public abstract class AbstractTokenException extends CoreException {

	public AbstractTokenException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
