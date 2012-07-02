package net.myconfig.service.model;

public class EnvironmentSummary extends Environment {
	
	private final int configCount;
	private final int valueCount;

	public EnvironmentSummary(String name, int configCount, int valueCount) {
		super(name);
		this.configCount = configCount;
		this.valueCount = valueCount;
	}
	
	public int getConfigCount() {
		return configCount;
	}
	
	public int getValueCount() {
		return valueCount;
	}

}
