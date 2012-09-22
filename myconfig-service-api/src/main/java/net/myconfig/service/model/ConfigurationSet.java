package net.myconfig.service.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationSet {

	private final String application;
	private final String environment;
	private final String version;

	private final List<ConfigurationValue> values;

	public ConfigurationSet(String application, String environment, String version, List<ConfigurationValue> values) {
		this.application = application;
		this.environment = environment;
		this.version = version;
		this.values = ImmutableList.copyOf(values);
	}

}
