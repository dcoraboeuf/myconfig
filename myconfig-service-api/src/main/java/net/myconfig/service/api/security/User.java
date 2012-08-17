package net.myconfig.service.api.security;

public class User {

	private final String name;
	private final boolean admin;
	private final boolean verified;
	private final boolean disabled;

	public User(String name, boolean admin, boolean verified, boolean disabled) {
		this.name = name;
		this.admin = admin;
		this.verified = verified;
		this.disabled = disabled;
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
	
	public boolean isDisabled() {
		return disabled;
	}

}
