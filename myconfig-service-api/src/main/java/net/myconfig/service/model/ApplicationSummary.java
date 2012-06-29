package net.myconfig.service.model;

public class ApplicationSummary {

	private final int id;
	private final String name;

	private final int versionCount;
	private final int keyCount;
	private final int environmentCount;
	private final int configCount;
	private final int valueCount;

	public ApplicationSummary(int id, String name, int versionCount, int keyCount, int environmentCount, int configCount, int valueCount) {
		this.id = id;
		this.name = name;
		this.versionCount = versionCount;
		this.keyCount = keyCount;
		this.environmentCount = environmentCount;
		this.configCount = configCount;
		this.valueCount = valueCount;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getVersionCount() {
		return versionCount;
	}

	public int getKeyCount() {
		return keyCount;
	}

	public int getEnvironmentCount() {
		return environmentCount;
	}

	public int getConfigCount() {
		return configCount;
	}

	public int getValueCount() {
		return valueCount;
	}

}
