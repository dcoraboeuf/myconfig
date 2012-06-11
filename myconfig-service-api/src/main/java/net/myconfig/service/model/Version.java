package net.myconfig.service.model;

public class Version {

	private final String name;

	public Version(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
