package net.myconfig.service.token;

import net.myconfig.core.model.TokenType;

public interface TokenService {

	String generateToken(TokenType type, String key);

	void checkToken(String token, TokenType type, String key);

	void consumesToken(String token, TokenType type, String key);

}
