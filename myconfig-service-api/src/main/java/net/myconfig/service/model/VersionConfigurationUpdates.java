package net.myconfig.service.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

public class VersionConfigurationUpdates {

	private final List<VersionConfigurationUpdate> updates;

	@JsonCreator
	public VersionConfigurationUpdates(@JsonProperty("updates") List<VersionConfigurationUpdate> updates) {
		this.updates = ImmutableList.copyOf(updates);
	}

	public List<VersionConfigurationUpdate> getUpdates() {
		return updates;
	}

}
