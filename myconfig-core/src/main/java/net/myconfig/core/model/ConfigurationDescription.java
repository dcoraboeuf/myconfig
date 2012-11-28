package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationDescription {

	private final List<Environment> environments;
	private final List<Key> keys;

	public ConfigurationDescription(List<Environment> environments, List<Key> keys) {
		this.environments = ImmutableList.copyOf(environments);
		this.keys = ImmutableList.copyOf(keys);
	}

}
