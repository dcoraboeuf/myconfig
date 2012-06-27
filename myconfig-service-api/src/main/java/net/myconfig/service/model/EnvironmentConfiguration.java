package net.myconfig.service.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class EnvironmentConfiguration {

	private final String name;
	private final Map<String, String> values;

	public EnvironmentConfiguration(String name, Map<String, String> values) {
		this.name = name;
		this.values = ImmutableMap.copyOf(values);
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getValues() {
		return values;
	}

}
