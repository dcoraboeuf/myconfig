package net.myconfig.core.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationDescription {

	private final List<Environment> environments;
	private final List<Key> keys;

	@JsonCreator
	public ConfigurationDescription(
			@JsonProperty("environments") List<Environment> environments,
			@JsonProperty("keys") List<Key> keys) {
		this.environments = ImmutableList.copyOf(environments);
		this.keys = ImmutableList.copyOf(keys);
	}

}
