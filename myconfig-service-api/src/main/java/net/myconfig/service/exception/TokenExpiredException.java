package net.myconfig.service.exception;

import net.myconfig.service.model.TokenType;


public class TokenExpiredException extends AbstractTokenException {

	public TokenExpiredException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
