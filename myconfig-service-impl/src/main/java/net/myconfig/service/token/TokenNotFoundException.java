package net.myconfig.service.token;

import net.myconfig.service.token.TokenService.TokenType;

public class TokenNotFoundException extends AbstractTokenException {

	public TokenNotFoundException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
