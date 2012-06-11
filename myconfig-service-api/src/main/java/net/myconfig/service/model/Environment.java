package net.myconfig.service.model;

public class Environment {

	private final String name;

	public Environment(String name) {
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
