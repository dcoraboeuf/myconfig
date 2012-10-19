package net.myconfig.core.model;

import java.util.Collection;
import java.util.Set;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableSet;

@Data
public class MatrixVersionConfiguration {

	private final String name;
	private final Set<String> keys;

	@JsonCreator
	public MatrixVersionConfiguration(
			@JsonProperty("name") String name,
			@JsonProperty("keys") Collection<String> keys) {
		this.name = name;
		this.keys = ImmutableSet.copyOf(keys);
	}

}
