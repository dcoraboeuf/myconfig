package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ConfigurationSet {

	private final List<ConfigurationValue> values;

	public ConfigurationSet(List<ConfigurationValue> values) {
		this.values = ImmutableList.copyOf(values);
	}

	public List<ConfigurationValue> getValues() {
		return values;
	}

}
