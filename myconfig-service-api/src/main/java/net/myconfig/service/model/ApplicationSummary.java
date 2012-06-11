package net.myconfig.service.model;

public class ApplicationSummary {

	private final int id;
	private final String name;

	public ApplicationSummary(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
