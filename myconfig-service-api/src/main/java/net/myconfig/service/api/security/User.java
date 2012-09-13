package net.myconfig.service.api.security;

public class User {

	private final String name;
	private final String displayName;
	private final String email;
	private final boolean admin;
	private final boolean verified;
	private final boolean disabled;

	public User(String name, String displayName, String email, boolean admin, boolean verified, boolean disabled) {
		this.name = name;
		this.displayName = displayName;
		this.email = email;
		this.admin = admin;
		this.verified = verified;
		this.disabled = disabled;
	}

	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getEmail() {
		return email;
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
