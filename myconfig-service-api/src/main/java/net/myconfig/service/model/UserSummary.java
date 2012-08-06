package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.myconfig.core.UserFunction;

public class UserSummary {

	private final String name;
	private final boolean admin;
	private final List<UserFunction> functions;

	public UserSummary(String name, boolean admin, List<UserFunction> functions) {
		this.name = name;
		this.admin = admin;
		this.functions = ImmutableList.copyOf(functions);
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin() {
		return admin;
	}
	
	public List<UserFunction> getFunctions() {
		return functions;
	}

}
