package net.myconfig.core.model;

import lombok.Data;

import org.joda.time.DateTime;

@Data
public class EventRecord {
	
	private final int id;
	private final String security;
	private final String user;
	private final DateTime creation;
	private final EventCategory category;
	private final String identifier;
	private final String message;

}
