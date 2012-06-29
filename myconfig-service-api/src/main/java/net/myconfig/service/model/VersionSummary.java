package net.myconfig.service.model;

public class VersionSummary extends Version {

	private final int keyCount;

	public VersionSummary(String name, int keyCount) {
		super(name);
		this.keyCount = keyCount;
	}

	public int getKeyCount() {
		return keyCount;
	}

}
