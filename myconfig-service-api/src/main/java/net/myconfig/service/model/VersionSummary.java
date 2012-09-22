package net.myconfig.service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VersionSummary extends Version {

	private final int keyCount;
	private final int configCount;
	private final int valueCount;

	public VersionSummary(String name, int keyCount, int configCount, int valueCount) {
		super(name);
		this.keyCount = keyCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

}
