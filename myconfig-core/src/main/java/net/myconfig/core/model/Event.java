package net.myconfig.core.model;

import static org.apache.commons.lang3.StringUtils.substring;
import lombok.Data;

import org.joda.time.DateTime;

@Data
public class Event {

	public static final int LENGTH_SECURITY = 10;
	public static final int LENGTH_USER = 80;
	public static final int LENGTH_CATEGORY = 80;
	public static final int LENGTH_IDENTIDIER = 80;
	public static final int LENGTH_OLDVALUE = 200;
	public static final int LENGTH_NEWVALUE = 200;
	public static final int LENGTH_MESSAGE = 600;
	
	private final int id;
	private final String security;
	private final String user;
	private final DateTime creation;
	private final String category;
	private final String identifier;
	private final String oldValue;
	private final String newValue;
	private final String message;
	
	public Event prepareForRecording () {
		return new Event(
				id,
				substring(security, 0, LENGTH_SECURITY),
				substring(user, 0, LENGTH_USER),
				creation,
				substring(category, 0, LENGTH_CATEGORY),
				substring(identifier, 0, LENGTH_IDENTIDIER),
				substring(oldValue, 0, LENGTH_OLDVALUE),
				substring(newValue, 0, LENGTH_NEWVALUE),
				substring(message, 0, LENGTH_MESSAGE));
	}

}
