package net.myconfig.service.impl;

import static org.apache.commons.lang3.StringUtils.substring;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.Event;
import net.myconfig.service.api.EventService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.db.SQLUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl extends AbstractDaoService implements EventService {

	private static final String MESSAGE = "message";
	private static final String NEWVALUE = "newvalue";
	private static final String OLDVALUE = "oldvalue";
	private static final String IDENTIFIER = "identifier";
	private static final String CATEGORY = "category";
	private static final String CREATION = "creation";
	private static final String USER = "user";
	private static final String SECURITY = "security";

	public static final int LENGTH_SECURITY = 10;
	public static final int LENGTH_USER = 80;
	public static final int LENGTH_CATEGORY = 80;
	public static final int LENGTH_IDENTIDIER = 80;
	public static final int LENGTH_OLDVALUE = 200;
	public static final int LENGTH_NEWVALUE = 200;
	public static final int LENGTH_MESSAGE = 600;
	
	private static final String EVENT_SAVE = "insert into events (security, user, creation, category, identifier, oldvalue, newvalue, message) values (:security, :user, :creation, :category, :identifier, :oldvalue, :newvalue, :message)";
	
	private final SecuritySelector securitySelector;

	@Autowired
	public EventServiceImpl(DataSource dataSource, Validator validator, SecuritySelector securitySelector) {
		super(dataSource, validator);
		this.securitySelector = securitySelector;
	}

	@Override
	@Transactional
	public void saveEvent(Event event) {
		// Gets the security information
		String security = securitySelector.getSecurityMode();
		String user = securitySelector.getCurrentUserName();
		if (user == null) {
			user = "-";
		}
		// Prepares the record
		// Saves into the database
		getNamedParameterJdbcTemplate().update(
				EVENT_SAVE,
				new MapSqlParameterSource()
					.addValue(SECURITY, substring(security, 0, LENGTH_SECURITY))
					.addValue(USER, substring(user, 0, LENGTH_USER))
					.addValue(CREATION, SQLUtils.now())
					.addValue(CATEGORY, substring(event.getCategory(), 0, LENGTH_CATEGORY))
					.addValue(IDENTIFIER, substring(event.getIdentifier(), 0, LENGTH_IDENTIDIER))
					.addValue(OLDVALUE, substring(event.getOldValue(), 0, LENGTH_OLDVALUE))
					.addValue(NEWVALUE, substring(event.getNewValue(), 0, LENGTH_NEWVALUE))
					.addValue(MESSAGE, substring(event.getMessage(), 0, LENGTH_MESSAGE))
				);
	}

}
