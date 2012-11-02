package net.myconfig.core.model;

import lombok.Data;

@Data
public class EventFilter {

	private static final int LIMIT_DEFAULT = 100;

	private int limit = LIMIT_DEFAULT;
	private int offset = 0;
	private String security;
	private String user;
	private EventCategory category;
	private EventAction action;
	private String identifier;
	private String application;
	private String environment;
	private String version;
	private String key;
	private String targetUser;
	private String function;
	private String message;

}
