package net.myconfig.core.model;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
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

}
