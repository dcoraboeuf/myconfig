package net.myconfig.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
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
	
	public Event(EventCategory category, EventAction action) {
		this.category = category;
		this.action = action;
		this.identifier = this.application = this.environment = this.version = this.key = null;
		this.targetUser = this.function = null;
		this.message = null;
	}

}
