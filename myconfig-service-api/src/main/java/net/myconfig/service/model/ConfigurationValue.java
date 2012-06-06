package net.myconfig.service.model;

public class ConfigurationValue {

	private final String key;
	private final String description;
	private final String value;

	public ConfigurationValue(String key, String description, String value) {
		this.key = key;
		this.description = description;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public String getValue() {
		return value;
	}

}
