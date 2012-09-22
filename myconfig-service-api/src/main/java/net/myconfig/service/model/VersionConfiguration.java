package net.myconfig.service.model;

import java.util.List;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class VersionConfiguration {

	private final int id;
	private final String name;
	private final String version;
	private final String previousVersion;
	private final String nextVersion;
	private final List<Key> keyList;
	private final List<IndexedValues<String>> environmentValuesPerKeyList;

	public VersionConfiguration(int id, String name, String version, String previousVersion, String nextVersion, List<Key> keyList, List<IndexedValues<String>> environmentValuesPerKeyList) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.previousVersion = previousVersion;
		this.nextVersion = nextVersion;
		this.keyList = ImmutableList.copyOf(keyList);
		this.environmentValuesPerKeyList = ImmutableList.copyOf(environmentValuesPerKeyList);
	}

}
