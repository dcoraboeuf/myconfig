package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationSet {

	private final String id;
	private final String name;
	private final String environment;
	private final String version;

	private final List<ConfigurationValue> values;

	public ConfigurationSet(String id, String name, String environment, String version, List<ConfigurationValue> values) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.version = version;
		this.values = ImmutableList.copyOf(values);
	}

}
