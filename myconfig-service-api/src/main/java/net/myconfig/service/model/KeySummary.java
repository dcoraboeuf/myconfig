package net.myconfig.service.model;

public class KeySummary extends Key {

	private final int versionNumber;

	public KeySummary(String name, String description, int versionNumber) {
		super(name, description);
		this.versionNumber = versionNumber;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

}
