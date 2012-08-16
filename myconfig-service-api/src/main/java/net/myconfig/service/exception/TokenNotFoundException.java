package net.myconfig.service.exception;

import net.myconfig.service.model.TokenType;


public class TokenNotFoundException extends AbstractTokenException {

	public TokenNotFoundException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
