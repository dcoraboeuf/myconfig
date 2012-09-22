package net.myconfig.service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EnvironmentSummary extends Environment {

	private final int configCount;
	private final int valueCount;

	public EnvironmentSummary(String name, int configCount, int valueCount) {
		super(name);
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

}
