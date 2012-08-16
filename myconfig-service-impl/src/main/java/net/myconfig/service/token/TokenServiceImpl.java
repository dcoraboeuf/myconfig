package net.myconfig.service.token;

import static net.myconfig.service.db.SQLColumns.CREATION;
import static net.myconfig.service.db.SQLColumns.TOKEN;
import static net.myconfig.service.db.SQLColumns.TOKENKEY;
import static net.myconfig.service.db.SQLColumns.TOKENTYPE;

import java.sql.Timestamp;
import java.util.UUID;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLUtils;
import net.myconfig.service.impl.AbstractDaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO Task to clean obsolete tokens after a while

@Service
public class TokenServiceImpl extends AbstractDaoService implements TokenService {

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

	private String createToken(TokenType type, String key) {
		String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
		return Sha512DigestUtils.shaHex(s);
	}

}
