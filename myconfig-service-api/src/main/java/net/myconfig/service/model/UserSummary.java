package net.myconfig.service.model;

import java.util.EnumSet;

import net.myconfig.core.UserFunction;

public class UserSummary {

	private final String name;
	private final boolean admin;
	private final boolean verified;
	private final EnumSet<UserFunction> functions;

	public UserSummary(String name, boolean admin, boolean verified, EnumSet<UserFunction> functions) {
		this.name = name;
		this.admin = admin;
		this.verified = verified;
		this.functions = functions;
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

	public EnumSet<UserFunction> getFunctions() {
		return functions;
	}

}
