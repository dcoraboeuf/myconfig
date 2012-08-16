package net.myconfig.service.token;

import net.myconfig.service.token.TokenService.TokenType;

public class TokenExpiredException extends AbstractTokenException {

	public TokenExpiredException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
