package net.myconfig.core.model;

import java.util.EnumSet;

import lombok.Data;
import net.myconfig.core.AppFunction;

@Data
public class UserApplicationRights {
	
	private final String id;
	private final String name;
	private final EnumSet<AppFunction> functions;

}
