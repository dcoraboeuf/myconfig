package net.myconfig.core.model;

import lombok.Data;

@Data
public class KeySummary {

	private final String name;
	private final String description;
	private final int versionCount;
	private final int configCount;
	private final int valueCount;

}
