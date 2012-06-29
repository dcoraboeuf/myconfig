package net.myconfig.service.model;

public class KeySummary extends Key {

	private final int versionCount;

	public KeySummary(String name, String description, int versionCount) {
		super(name, description);
		this.versionCount = versionCount;
	}

	public int getVersionCount() {
		return versionCount;
	}

}
