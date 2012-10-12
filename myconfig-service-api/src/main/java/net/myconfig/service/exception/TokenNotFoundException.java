package net.myconfig.service.exception;

import net.myconfig.core.model.TokenType;


public class TokenNotFoundException extends AbstractTokenException {

	public TokenNotFoundException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
