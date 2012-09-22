package net.myconfig.service.model;

import lombok.Data;

@Data
public class ConditionalValue {

	private final boolean enabled;
	private final String value;

}
