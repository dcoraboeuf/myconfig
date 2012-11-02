package net.myconfig.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class EventFilter {
	
	private static final int LIMIT_DEFAULT = 100;
	
	private final int limit;
	private final int offset;
	
	public EventFilter () {
		this.limit = LIMIT_DEFAULT;
		this.offset = 0;
	}

}
