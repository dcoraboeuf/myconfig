package net.myconfig.core.model;

import lombok.Data;

@Data
public class VersionSummary {

	private final String name;
	private final int keyCount;
	private final int configCount;
	private final int valueCount;

}
