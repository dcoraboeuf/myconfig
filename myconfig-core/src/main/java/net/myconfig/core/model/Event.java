package net.myconfig.core.model;

import lombok.Data;

@Data
public class Event {
	
	private final String category;
	private final String identifier;
	private final String oldValue;
	private final String newValue;
	private final String message;

}