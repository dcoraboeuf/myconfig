package net.myconfig.core.model;

import java.util.List;

import lombok.Data;

@Data
public class ConfigurationUpdates {

	private final List<ConfigurationUpdate> updates;

//	public ConfigurationUpdates(List<ConfigurationUpdate> updates) {
//	FIXME	this.updates = ImmutableList.copyOf(updates);
//	}

}
