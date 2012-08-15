package net.myconfig.service.token;

import java.util.UUID;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import net.myconfig.service.impl.AbstractDaoService;

@Service
public class TokenServiceImpl extends AbstractDaoService implements TokenService {

	@Autowired
	public TokenServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	public String generateToken(TokenType type, String key) {
		// Generates the token
		String token = createToken(type, key);
		// FIXME Saves the token
		// OK
		return token;
	}

	private String createToken(TokenType type, String key) {
		String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
		return Sha512DigestUtils.shaHex(s);
	}

}
