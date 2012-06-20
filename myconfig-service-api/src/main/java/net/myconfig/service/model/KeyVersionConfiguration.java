package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class KeyVersionConfiguration {

	private final int id;
	private final String name;
	private final List<VersionConfiguration> versionConfigurationList;
	private final List<Key> keyList;

	public KeyVersionConfiguration(int id, String name, List<VersionConfiguration> versionConfigurationList, List<Key> keyList) {
		this.id = id;
		this.name = name;
		this.versionConfigurationList = ImmutableList.copyOf(versionConfigurationList);
		this.keyList = ImmutableList.copyOf(keyList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<VersionConfiguration> getVersionConfigurationList() {
		return versionConfigurationList;
	}

	public List<Key> getKeyList() {
		return keyList;
	}

}
