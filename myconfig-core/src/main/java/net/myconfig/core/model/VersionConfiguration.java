package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class VersionConfiguration {

	private final String id;
	private final String name;
	private final String version;
	private final String previousVersion;
	private final String nextVersion;
	private final List<Key> keyList;
	private final List<IndexedValues<String>> environmentValuesPerKeyList;

	@JsonCreator
	public VersionConfiguration(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("version") String version,
			@JsonProperty("previousVersion") String previousVersion,
			@JsonProperty("nextVersion") String nextVersion,
			@JsonProperty("keyList") List<Key> keyList,
			@JsonProperty("environmentValuesPerKeyList") List<IndexedValues<String>> environmentValuesPerKeyList) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.previousVersion = previousVersion;
		this.nextVersion = nextVersion;
		this.keyList = ImmutableList.copyOf(keyList);
		this.environmentValuesPerKeyList = ImmutableList.copyOf(environmentValuesPerKeyList);
	}

}
