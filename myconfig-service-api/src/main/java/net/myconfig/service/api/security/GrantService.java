package net.myconfig.service.api.security;

import java.util.EnumSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;

public interface GrantService {

	boolean hasUserFunction(String name, UserFunction fn);

	boolean hasAppFunction(String application, String name, AppFunction fn);

	boolean hasEnvFunction(String application, String name, String environment, EnvFunction fn);

	EnumSet<UserFunction> getUserFunctions(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	Ack appFunctionAdd(String application, String user, AppFunction fn);

	Ack appFunctionRemove(String application, String user, AppFunction fn);

	EnumSet<AppFunction> getAppFunctions(String application, String user);

	Ack envFunctionAdd(String application, String user, String environment, EnvFunction fn);

	Ack envFunctionRemove(String application, String user, String environment, EnvFunction fn);

	EnumSet<EnvFunction> getEnvFunctions(String application, String user, String environment);

}
