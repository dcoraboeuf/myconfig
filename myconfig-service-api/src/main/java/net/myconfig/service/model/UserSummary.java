package net.myconfig.service.model;

import java.util.EnumSet;

import net.myconfig.core.UserFunction;

public class UserSummary {

	private final String name;
	private final String displayName;
	private final boolean admin;
	private final boolean verified;
	private final boolean disabled;
	private final EnumSet<UserFunction> functions;

	public UserSummary(String name, String displayName, boolean admin, boolean verified, boolean disabled, EnumSet<UserFunction> functions) {
		this.name = name;
		this.displayName = displayName;
		this.admin = admin;
		this.verified = verified;
		this.disabled = disabled;
		this.functions = functions;
	}

	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
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

	public EnumSet<UserFunction> getFunctions() {
		return functions;
	}

}
