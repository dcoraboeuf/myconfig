package net.myconfig.service.token;


public interface TokenService {
	
	public enum TokenType {
		NEW_USER
	}

	String generateToken(TokenType type, String key);

}
