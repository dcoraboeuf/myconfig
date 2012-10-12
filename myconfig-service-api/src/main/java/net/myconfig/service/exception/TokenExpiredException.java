package net.myconfig.service.exception;

import net.myconfig.core.model.TokenType;


public class TokenExpiredException extends AbstractTokenException {

	public TokenExpiredException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
