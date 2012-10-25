package net.myconfig.core.model;

import java.util.EnumSet;

import lombok.Data;
import net.myconfig.core.EnvFunction;


@Data
public class EnvironmentUserRights {
	
	private final String name;
	private final String displayName;
	private final EnumSet<EnvFunction> functions;

}
