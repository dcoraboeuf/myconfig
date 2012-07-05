package net.myconfig.service.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

public class ConfigurationUpdates {

	private final List<ConfigurationUpdate> updates;

	@JsonCreator
	public ConfigurationUpdates(@JsonProperty("updates") List<ConfigurationUpdate> updates) {
		this.updates = ImmutableList.copyOf(updates);
	}

	public List<ConfigurationUpdate> getUpdates() {
		return updates;
	}

}
