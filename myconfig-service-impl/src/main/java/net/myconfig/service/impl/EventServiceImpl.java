package net.myconfig.service.impl;

import static org.apache.commons.lang3.StringUtils.substring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.EventFilter;
import net.myconfig.core.model.EventRecord;
import net.myconfig.service.api.EventService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.db.SQLUtils;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl extends AbstractDaoService implements EventService {

	private static final String ID = "id";
	private static final String MESSAGE = "message";
	private static final String IDENTIFIER = "identifier";
	private static final String APPLICATION = "application";
	private static final String ENVIRONMENT = "environment";
	private static final String VERSION = "version";
	private static final String KEY = "appkey";
	private static final String CATEGORY = "category";
	private static final String ACTION = "action";
	private static final String CREATION = "creation";
	private static final String USER = "user";
	private static final String SECURITY = "security";
	private static final String TARGET_USER = "targetUser";
	private static final String FUNCTION = "fn";

	public static final int LENGTH_SECURITY = 10;
	public static final int LENGTH_USER = 80;
	public static final int LENGTH_IDENTIFIER = 80;
	public static final int LENGTH_MESSAGE = 600;
	public static final int LENGTH_FUNCTION = 20;
	
	private static final String EVENT_SAVE = "insert into events (security, user, creation, category, action, identifier, application, environment, version, appkey, message, targetUser, fn) values (:security, :user, :creation, :category, :action, :identifier, :application, :environment, :version, :appkey, :message, :targetUser, :fn)";
	
	private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
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
					.addValue(CATEGORY, event.getCategory().name())
					.addValue(ACTION, event.getAction().name())
					.addValue(IDENTIFIER, substring(event.getIdentifier(), 0, LENGTH_IDENTIFIER))
					.addValue(APPLICATION, substring(event.getApplication(), 0, LENGTH_IDENTIFIER))
					.addValue(ENVIRONMENT, substring(event.getEnvironment(), 0, LENGTH_IDENTIFIER))
					.addValue(VERSION, substring(event.getVersion(), 0, LENGTH_IDENTIFIER))
					.addValue(KEY, substring(event.getKey(), 0, LENGTH_IDENTIFIER))
					.addValue(MESSAGE, substring(event.getMessage(), 0, LENGTH_MESSAGE))
					.addValue(TARGET_USER, substring(event.getTargetUser(), 0, LENGTH_USER))
					.addValue(FUNCTION, substring(event.getFunction(), 0, LENGTH_FUNCTION))
				);
	}
	
	@Override
	// FIXME Secures for administrator only
	public Collection<EventRecord> filter(EventFilter eventFilter) {
		// SQL to build
		StringBuilder sql = new StringBuilder("SELECT * FROM events");
		// Associated parameters
		MapSqlParameterSource params = new MapSqlParameterSource();
		// Ordering
		sql.append(" ORDER BY ID DESC");
		// Limit
		sql.append(" LIMIT " + eventFilter.getLimit() + " OFFSET " + eventFilter.getOffset());
		// Log
		logger.debug("Audit SQL: {}", sql);
		// Query
		return getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<EventRecord>() {
			@Override
			public EventRecord mapRow(ResultSet rs, int index) throws SQLException {
				// Creation time
				DateTime utcCreation = SQLUtils.getDateTime(rs, CREATION);
				// Event
				Event event = new Event(SQLUtils.getEnum(EventCategory.class, rs, CATEGORY), SQLUtils.getEnum(EventAction.class, rs, ACTION));
				event = event.withIdentifier(rs.getString(IDENTIFIER));
				event = event.withApplication(rs.getString(APPLICATION));
				event = event.withEnvironment(rs.getString(ENVIRONMENT));
				event = event.withVersion(rs.getString(VERSION));
				event = event.withKey(rs.getString(KEY));
				event = event.withMessage(rs.getString(MESSAGE));
				event = event.withTargetUser(rs.getString(TARGET_USER));
				event = event.withFunction(rs.getString(FUNCTION));
				// Record
				return new EventRecord(rs.getInt(ID), rs.getString(SECURITY), rs.getString(USER), utcCreation, event);
			}
		});
	}

}
