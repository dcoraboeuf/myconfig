package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class KeyConfiguration {

	private final int id;
	private final String name;
	private final Key key;
	private final String previousKey;
	private final String nextKey;
	private final List<Version> versionList;
	private final List<IndexedValues<String>> environmentValuesPerVersionList;

	public KeyConfiguration(int id, String name, Key key, String previousKey, String nextKey, List<Version> versionList, List<IndexedValues<String>> environmentValuesPerVersionList) {
		this.id = id;
		this.name = name;
		this.key = key;
		this.previousKey = previousKey;
		this.nextKey = nextKey;
		this.versionList = ImmutableList.copyOf(versionList);
		this.environmentValuesPerVersionList = ImmutableList.copyOf(environmentValuesPerVersionList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Key getKey() {
		return key;
	}

	public String getPreviousKey() {
		return previousKey;
	}

	public String getNextKey() {
		return nextKey;
	}

	public List<Version> getVersionList() {
		return versionList;
	}

	public List<IndexedValues<String>> getEnvironmentValuesPerVersionList() {
		return environmentValuesPerVersionList;
	}

}
