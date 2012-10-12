package net.myconfig.service.token;

import static net.myconfig.service.db.SQLColumns.CREATION;
import static net.myconfig.service.db.SQLColumns.TOKEN;
import static net.myconfig.service.db.SQLColumns.TOKENKEY;
import static net.myconfig.service.db.SQLColumns.TOKENTYPE;

import java.sql.Timestamp;
import java.util.UUID;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.TokenType;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLUtils;
import net.myconfig.service.exception.TokenExpiredException;
import net.myconfig.service.exception.TokenNotFoundException;
import net.myconfig.service.impl.AbstractDaoService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO Task to clean obsolete tokens after a while

@Service
public class TokenServiceImpl extends AbstractDaoService implements TokenService {

	private static final int EXPIRATION_DELAY = 15;

	@Autowired
	public TokenServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional
	public String generateToken(TokenType type, String key) {
		// Generates the token
		String token = createToken(type, key);
		// Creation date
		Timestamp creation = SQLUtils.now();
		// Saves the token
		getNamedParameterJdbcTemplate().update(
				SQL.TOKEN_SAVE,
				new MapSqlParameterSource()
					.addValue(TOKEN, token)
					.addValue(TOKENTYPE, type.name())
					.addValue(TOKENKEY, key)
					.addValue(CREATION, creation));
		// OK
		return token;
	}
	
	@Override
	@Transactional(readOnly = true)
	public void checkToken(String token, TokenType type, String key) {
			try {
				Timestamp creation = getNamedParameterJdbcTemplate().queryForObject(
						SQL.TOKEN_CHECK, 
						new MapSqlParameterSource()
							.addValue(TOKEN, token)
							.addValue(TOKENTYPE, type.name())
							.addValue(TOKENKEY, key),
						Timestamp.class);
				DateTime utcCreation = new DateTime(creation.getTime(), DateTimeZone.UTC);
				DateTime utcNow = DateTime.now(DateTimeZone.UTC);
				Days days = Days.daysBetween(utcCreation, utcNow);
				if (days.isGreaterThan(Days.days(EXPIRATION_DELAY))) {
					throw new TokenExpiredException (token, type, key);
				}
			} catch (EmptyResultDataAccessException ex) {
				throw new TokenNotFoundException (token, type, key);
			}
	}
	
	@Override
	@Transactional
	public void consumesToken(String token, TokenType type, String key) {
		// Checks the token
		checkToken(token, type, key);
		// Deletes the token
		getNamedParameterJdbcTemplate().update(
				SQL.TOKEN_DELETE,
				new MapSqlParameterSource()
					.addValue(TOKENTYPE, type.name())
					.addValue(TOKENKEY, key));
	}

	private String createToken(TokenType type, String key) {
		String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
		return Sha512DigestUtils.shaHex(s);
	}

}
