package net.myconfig.core.model;

import lombok.Data;

@Data
public class Event {
	
	private final EventCategory category;
	private final String identifier;
	private final String message;

}
