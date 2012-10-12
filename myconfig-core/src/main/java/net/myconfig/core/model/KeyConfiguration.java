package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
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

}
