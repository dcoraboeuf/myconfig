package net.myconfig.core.model;

import lombok.Data;

@Data
public class Event {

	private final EventCategory category;
	private final EventAction action;
	private final String identifier;
	private final String application;
	private final String environment;
	private final String version;
	private final String key;
	private final String targetUser;
	private final String function;
	private final String message;

	public Event(EventCategory category, EventAction action, String identifier, String message) {
		this.category = category;
		this.action = action;
		this.identifier = identifier;
		this.message = message;
		this.application = this.environment = this.version = this.key = null;
		this.targetUser = this.function = null;
	}

	public Event(EventCategory category, EventAction action, String application, String environment, String version, String key, String message) {
		this.category = category;
		this.action = action;
		this.identifier = null;
		this.application = application;
		this.environment = environment;
		this.version = version;
		this.key = key;
		this.message = message;
		this.targetUser = this.function = null;
	}
	
	protected Event (Event e, String user, String function) {
		this.category = e.category;
		this.action = e.action;
		this.identifier = e.identifier;
		this.application = e.application;
		this.environment = e.environment;
		this.version = e.version;
		this.key = e.key;
		this.message = e.message;
		this.targetUser = user;
		this.function = function;
	}
	
	public Event withUser (String user) {
		return new Event(this, user, this.function);
	}
	
	public Event withFunction (String fn) {
		return new Event(this, this.targetUser, fn);
	}

}
