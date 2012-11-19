package net.myconfig.service.impl;

import static org.apache.commons.lang3.StringUtils.substring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventAction;
import net.myconfig.core.model.EventCategory;
import net.myconfig.core.model.EventFilter;
import net.myconfig.core.model.EventRecord;
import net.myconfig.service.api.EventService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.db.SQLUtils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	private static final String EVENT_CLEAR = "delete from events where creation < :creation";
	private static final String EVENT_CLEAR_ALL = "delete from events";
	
	private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	private final SecuritySelector securitySelector;

	@Autowired
	public EventServiceImpl(DataSource dataSource, Validator validator, SecuritySelector securitySelector) {
		super(dataSource, validator);
		this.securitySelector = securitySelector;
	}
	
	@Override
	@Transactional
	public int clean(int retentionDays) {
		// Date cut-off
		Timestamp cutOff = SQLUtils.toTimestamp(DateTime.now(DateTimeZone.UTC).minusDays(retentionDays));
		// SQL
		int count = getNamedParameterJdbcTemplate().update(EVENT_CLEAR, new MapSqlParameterSource(CREATION, cutOff));
		// OK
		return count;
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
	@Transactional
	public void clearAll() {
		SecurityUtils.checkIsAdmin(securitySelector);
		// Clear all
		getJdbcTemplate().update(EVENT_CLEAR_ALL);
		// Inserts an event
		saveEvent(new Event(EventCategory.AUDIT, EventAction.DELETE));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<EventRecord> filter(EventFilter filter) {
		SecurityUtils.checkIsAdmin(securitySelector);
		// SQL to build
		StringBuilder sql = new StringBuilder("SELECT * FROM events");
		// Associated parameters
		MapSqlParameterSource params = new MapSqlParameterSource();
		// Criteria
		AtomicInteger count = new AtomicInteger(0);
		filter (count, sql, params, IDENTIFIER, filter.getIdentifier());
		filter (count, sql, params, APPLICATION, filter.getApplication());
		filter (count, sql, params, ENVIRONMENT, filter.getEnvironment());
		filter (count, sql, params, VERSION, filter.getVersion());
		filter (count, sql, params, KEY, filter.getKey());
		filter (count, sql, params, TARGET_USER, filter.getTargetUser());
		filter (count, sql, params, FUNCTION, filter.getFunction());
		filter (count, sql, params, MESSAGE, filter.getMessage());
		filter (count, sql, params, SECURITY, filter.getSecurity());
		filter (count, sql, params, USER, filter.getUser());
		filter (count, sql, params, CATEGORY, filter.getCategory());
		filter (count, sql, params, ACTION, filter.getAction());
		// Ordering
		sql.append(" ORDER BY ID DESC");
		// Limit
		sql.append(" LIMIT " + filter.getLimit() + " OFFSET " + filter.getOffset());
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

	protected void filter(AtomicInteger count, StringBuilder sql, MapSqlParameterSource params, String column, String value) {
		filter (count, sql, params, true, column, value);
	}

	protected void filter(AtomicInteger count, StringBuilder sql, MapSqlParameterSource params, String column, Enum<?> value) {
		filter (count, sql, params, false, column, value != null ? value.name() : null);
	}

	protected void filter(AtomicInteger count, StringBuilder sql, MapSqlParameterSource params, boolean like, String column, String value) {
		if (StringUtils.isNotBlank(value)) {
		// SQL
			if (count.getAndIncrement() == 0) {
				sql.append(" WHERE ");
			} else {
				sql.append(" AND ");
			}
			String operator = like ? "LIKE" : "=";
			sql.append("(").append(column).append(" ").append(operator).append(" :").append(column).append(")");
			// Value
			if (like && !StringUtils.contains(value, "%")) {
				value = "%" + value + "%";
			}
			params.addValue(column, value);
		}
	}

}
