package net.myconfig.service.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class VersionConfigurationUpdate {

	private final String environment;
	private final String key;
	private final String value;

	@JsonCreator
	public VersionConfigurationUpdate( @JsonProperty("environment") String environment, @JsonProperty("key") String key, @JsonProperty("value") String value) {
		this.environment = environment;
		this.key = key;
		this.value = value;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
