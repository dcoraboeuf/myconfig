package net.myconfig.core.model;

import lombok.Data;

@Data
public class ConfigurationUpdate {

	private final String environment;
	private final String version;
	private final String key;
	private final String value;

}
