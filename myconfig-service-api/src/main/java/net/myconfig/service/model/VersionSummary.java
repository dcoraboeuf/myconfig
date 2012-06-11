package net.myconfig.service.model;

public class VersionSummary extends Version {

	private final int keyNumber;

	public VersionSummary(String name, int keyNumber) {
		super(name);
		this.keyNumber = keyNumber;
	}

	public int getKeyNumber() {
		return keyNumber;
	}

}
