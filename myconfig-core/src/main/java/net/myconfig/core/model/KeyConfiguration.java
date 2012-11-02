package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class KeyConfiguration {

	private final String id;
	private final String name;
	private final Key key;
	private final String previousKey;
	private final String nextKey;
	private final List<Version> versionList;
	private final List<IndexedValues<String>> environmentValuesPerVersionList;

	@JsonCreator
	public KeyConfiguration(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("key") Key key,
			@JsonProperty("previousKey") String previousKey,
			@JsonProperty("nextKey") String nextKey,
			@JsonProperty("versionList") List<Version> versionList,
			@JsonProperty("environmentValuesPerVersionList") List<IndexedValues<String>> environmentValuesPerVersionList) {
		this.id = id;
		this.name = name;
		this.key = key;
		this.previousKey = previousKey;
		this.nextKey = nextKey;
		this.versionList = ImmutableList.copyOf(versionList);
		this.environmentValuesPerVersionList = ImmutableList.copyOf(environmentValuesPerVersionList);
	}

}
