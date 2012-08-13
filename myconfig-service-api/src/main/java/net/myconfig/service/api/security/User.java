package net.myconfig.service.api.security;

public class User {

	private final String name;
	private final boolean admin;
	private final boolean verified;

	public User(String name, boolean admin, boolean verified) {
		this.name = name;
		this.admin = admin;
		this.verified = verified;
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isVerified() {
		return verified;
	}

}
