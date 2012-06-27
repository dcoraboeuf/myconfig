package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class VersionConfiguration {

	private final int id;
	private final String name;
	private final String version;
	private final String previousVersion;
	private final String nextVersion;
	private final List<Key> keyList;
	private final List<EnvironmentConfiguration> environmentConfigurationList;

	public VersionConfiguration(int id, String name, String version, String previousVersion, String nextVersion, List<Key> keyList, List<EnvironmentConfiguration> environmentConfigurationList) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.previousVersion = previousVersion;
		this.nextVersion = nextVersion;
		this.keyList = ImmutableList.copyOf(keyList);
		this.environmentConfigurationList = ImmutableList.copyOf(environmentConfigurationList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Key> getKeyList() {
		return keyList;
	}

	public String getVersion() {
		return version;
	}

	public String getPreviousVersion() {
		return previousVersion;
	}

	public String getNextVersion() {
		return nextVersion;
	}

	public List<EnvironmentConfiguration> getEnvironmentConfigurationList() {
		return environmentConfigurationList;
	}

}
