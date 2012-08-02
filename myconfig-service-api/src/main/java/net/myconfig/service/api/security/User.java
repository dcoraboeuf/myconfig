package net.myconfig.service.api.security;

public class User {

	private final String name;
	private final boolean admin;

	public User(String name, boolean admin) {
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
