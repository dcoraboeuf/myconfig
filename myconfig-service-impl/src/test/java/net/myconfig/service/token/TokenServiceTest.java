package net.myconfig.service.token;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import net.myconfig.service.token.TokenService.TokenType;
import net.myconfig.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private TokenService tokenService;
	
	@Test
	public void save() throws DataSetException, SQLException {
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		assertRecordExists("select * from tokens where token = '%s' and tokentype = '%s' and tokenkey = '%s'", token, TokenType.NEW_USER.name(), "myuser");
	}

}
