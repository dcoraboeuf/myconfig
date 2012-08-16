package net.myconfig.service.token;

import net.myconfig.service.exception.CoreException;
import net.myconfig.service.token.TokenService.TokenType;

public abstract class AbstractTokenException extends CoreException {

	public AbstractTokenException(String token, TokenType type, String key) {
		super(token, type, key);
	}

}
