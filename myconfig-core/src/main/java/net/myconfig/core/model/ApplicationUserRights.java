package net.myconfig.core.model;

import java.util.EnumSet;

import lombok.Data;
import net.myconfig.core.AppFunction;


@Data
public class ApplicationUserRights {
	
	private final String name;
	private final String displayName;
	private final EnumSet<AppFunction> functions;

}
