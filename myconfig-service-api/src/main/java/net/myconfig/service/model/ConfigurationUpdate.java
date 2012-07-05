package net.myconfig.service.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class ConfigurationUpdate {

	private final String environment;
	private final String version;
	private final String key;
	private final String value;

	@JsonCreator
	public ConfigurationUpdate(
			@JsonProperty("environment") String environment,
			@JsonProperty("version") String version,
			@JsonProperty("key") String key,
			@JsonProperty("value") String value) {
		this.environment = environment;
		this.version = version;
		this.key = key;
		this.value = value;
	}

	public String getEnvironment() {
		return environment;
	}
	
	public String getVersion() {
		return version;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
