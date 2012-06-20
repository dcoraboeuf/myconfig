package net.myconfig.service.model;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class VersionConfiguration {

	private final String name;
	private final Set<String> keys;

	public VersionConfiguration(String name, Set<String> keys) {
		this.name = name;
		this.keys = ImmutableSet.copyOf(keys);
	}

	public String getName() {
		return name;
	}

	public Set<String> getKeys() {
		return keys;
	}

}
