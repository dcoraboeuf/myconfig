package net.myconfig.core.model;

import lombok.Data;

import org.joda.time.DateTime;

@Data
public class EventRecord {
	
	private final int id;
	private final EventCategory security;
	private final String user;
	private final DateTime creation;
	private final String category;
	private final String identifier;
	private final String oldValue;
	private final String newValue;
	private final String message;

}
