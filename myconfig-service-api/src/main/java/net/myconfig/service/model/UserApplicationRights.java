package net.myconfig.service.model;

import java.util.EnumSet;

import lombok.Data;
import net.myconfig.core.AppFunction;

@Data
public class UserApplicationRights {
	
	private final int id;
	private final String name;
	private final EnumSet<AppFunction> functions;

}
