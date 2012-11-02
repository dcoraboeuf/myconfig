package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class EnvironmentConfiguration {

	private final String id;
	private final String name;
	private final String environment;
	private final String previousEnvironment;
	private final String nextEnvironment;
	private final List<Key> keyList;
	private final List<IndexedValues<ConditionalValue>> versionValuesPerKeyList;

	@JsonCreator
	public EnvironmentConfiguration(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("environment") String environment,
			@JsonProperty("previousEnvironment") String previousEnvironment,
			@JsonProperty("nextEnvironment") String nextEnvironment,
			@JsonProperty("keyList") List<Key> keyList,
			@JsonProperty("versionValuesPerKeyList") List<IndexedValues<ConditionalValue>> versionValuesPerKeyList) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.previousEnvironment = previousEnvironment;
		this.nextEnvironment = nextEnvironment;
		this.keyList = ImmutableList.copyOf(keyList);
		this.versionValuesPerKeyList = ImmutableList.copyOf(versionValuesPerKeyList);
	}

}
