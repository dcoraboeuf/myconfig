package net.myconfig.service.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class IndexedValues {

	private final String name;
	private final Map<String, String> values;

	public IndexedValues(String name, Map<String, String> values) {
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
