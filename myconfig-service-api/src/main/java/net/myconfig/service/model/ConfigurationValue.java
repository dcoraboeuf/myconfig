package net.myconfig.service.model;

import lombok.Data;

@Data
public class ConfigurationValue {

	private final String key;
	private final String description;
	private final String value;

}
