package net.myconfig.service.model;

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

	public int getKeyCount() {
		return keyCount;
	}
	
	public int getConfigCount() {
		return configCount;
	}
	
	public int getValueCount() {
		return valueCount;
	}

}
