package net.myconfig.service.model;

public class UserSummary {

	private final String name;
	private final boolean admin;

	public UserSummary(String name, boolean admin) {
		this.name = name;
		this.admin = admin;
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin() {
		return admin;
	}

}
