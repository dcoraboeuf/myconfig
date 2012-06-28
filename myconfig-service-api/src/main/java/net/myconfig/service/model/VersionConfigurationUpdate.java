package net.myconfig.service.model;

public class VersionConfigurationUpdate {

	private final String environment;
	private final String key;
	private final String value;

	public VersionConfigurationUpdate(String environment, String key, String value) {
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
