package net.myconfig.service.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class IndexedValues<T> {

	private final String name;
	private final Map<String, T> values;

	public IndexedValues(String name, Map<String, T> values) {
		this.name = name;
		this.values = ImmutableMap.copyOf(values);
	}

	public String getName() {
		return name;
	}

	public Map<String, T> getValues() {
		return values;
	}

}
