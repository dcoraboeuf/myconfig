package net.myconfig.core.model;

import java.util.EnumSet;

import lombok.Data;

import net.myconfig.core.UserFunction;

@Data
public class UserSummary {

	private final String name;
	private final String displayName;
	private final String email;
	private final boolean admin;
	private final boolean verified;
	private final boolean disabled;
	private final EnumSet<UserFunction> functions;

}
