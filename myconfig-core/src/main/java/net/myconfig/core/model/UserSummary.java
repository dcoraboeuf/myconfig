package net.myconfig.core.model;

import java.util.EnumSet;

import lombok.AllArgsConstructor;
import lombok.Data;

import net.myconfig.core.UserFunction;

@Data
@AllArgsConstructor
public class UserSummary {

	private final boolean anyUser;
	private final String name;
	private final String displayName;
	private final String email;
	private final boolean admin;
	private final boolean verified;
	private final boolean disabled;
	private final EnumSet<UserFunction> functions;
	
	public UserSummary (EnumSet<UserFunction> functions) {
		this(true, "*", null, null, false, true, true, functions);
	}

}
