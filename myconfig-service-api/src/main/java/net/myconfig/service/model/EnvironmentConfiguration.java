package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class EnvironmentConfiguration {

	private final int id;
	private final String name;
	private final String environment;
	private final String previousEnvironment;
	private final String nextEnvironment;
	private final List<Key> keyList;
	private final List<IndexedValues<ConditionalValue>> versionValuesPerKeyList;

	public EnvironmentConfiguration(int id, String name, String environment, String previousEnvironment, String nextEnvironment, List<Key> keyList, List<IndexedValues<ConditionalValue>> versionValuesPerKeyList) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.previousEnvironment = previousEnvironment;
		this.nextEnvironment = nextEnvironment;
		this.keyList = ImmutableList.copyOf(keyList);
		this.versionValuesPerKeyList = ImmutableList.copyOf(versionValuesPerKeyList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getPreviousEnvironment() {
		return previousEnvironment;
	}

	public String getNextEnvironment() {
		return nextEnvironment;
	}

	public List<Key> getKeyList() {
		return keyList;
	}

	public List<IndexedValues<ConditionalValue>> getVersionValuesPerKeyList() {
		return versionValuesPerKeyList;
	}

}
