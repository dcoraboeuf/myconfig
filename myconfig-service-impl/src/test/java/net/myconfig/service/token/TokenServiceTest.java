package net.myconfig.service.token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import net.myconfig.core.model.TokenType;
import net.myconfig.service.exception.TokenExpiredException;
import net.myconfig.service.exception.TokenNotFoundException;
import net.myconfig.test.AbstractIntegrationTest;
import net.myconfig.test.DBUnitHelper;

import org.dbunit.dataset.DataSetException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

	@Test
	public void check() {
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		tokenService.checkToken(token, TokenType.NEW_USER, "myuser");
	}

	@Test(expected = TokenNotFoundException.class)
	public void check_no_token() {
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		tokenService.checkToken("xxx", TokenType.NEW_USER, "myuser");
	}

	@Test(expected = TokenNotFoundException.class)
	public void check_no_user() {
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		tokenService.checkToken(token, TokenType.NEW_USER, "userx");
	}

	@Test(expected = TokenExpiredException.class)
	public void check_expired() throws SQLException {
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		Connection c = DBUnitHelper.getConnection().getConnection();
		try {
			PreparedStatement ps = c.prepareStatement("update tokens set creation = ? where token = ?");
			try {
				Timestamp creation = new Timestamp(DateTime.now(DateTimeZone.UTC).minusDays(30).getMillis());
				ps.setTimestamp(1, creation);
				ps.setString(2, token);
				int count = ps.executeUpdate();
				assertEquals (1, count);
			} finally {
				ps.close();
			}
			c.commit();
		} finally {
			c.close();
		}
		tokenService.checkToken(token, TokenType.NEW_USER, "myuser");
	}

	@Test
	public void consumes() throws DataSetException, SQLException {
		tokenService.generateToken(TokenType.NEW_USER, "myuser");
		tokenService.generateToken(TokenType.NEW_USER, "myuser");
		String token = tokenService.generateToken(TokenType.NEW_USER, "myuser");
		assertNotNull(token);
		assertRecordExists("select * from tokens where token = '%s' and tokentype = '%s' and tokenkey = '%s'", token, TokenType.NEW_USER.name(), "myuser");
		tokenService.consumesToken(token, TokenType.NEW_USER, "myuser");
		assertRecordNotExists("select * from tokens where tokentype = '%s' and tokenkey = '%s'", TokenType.NEW_USER.name(), "myuser");
	}

}
