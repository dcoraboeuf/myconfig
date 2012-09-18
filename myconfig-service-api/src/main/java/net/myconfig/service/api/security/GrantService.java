package net.myconfig.service.api.security;

import java.util.EnumSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;

public interface GrantService {

	boolean hasUserFunction(String name, UserFunction fn);

	boolean hasAppFunction(String name, int application, AppFunction fn);

	boolean hasEnvFunction(String name, int application, String environment, EnvFunction fn);

	EnumSet<UserFunction> getUserFunctions(String name);

}
