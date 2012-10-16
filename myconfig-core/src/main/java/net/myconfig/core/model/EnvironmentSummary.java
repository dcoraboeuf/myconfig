package net.myconfig.core.model;

import lombok.Data;

@Data
public class EnvironmentSummary {

	private final String name;
	private final int configCount;
	private final int valueCount;

}
