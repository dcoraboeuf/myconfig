package net.myconfig.service.model;

public class KeySummary extends Key {

	private final int versionCount;
	private final int configCount;
	private final int valueCount;

	public KeySummary(String name, String description, int versionCount, int configCount, int valueCount) {
		super(name, description);
		this.versionCount = versionCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

	public int getVersionCount() {
		return versionCount;
	}
	
	public int getConfigCount() {
		return configCount;
	}
	
	public int getValueCount() {
		return valueCount;
	}

}
