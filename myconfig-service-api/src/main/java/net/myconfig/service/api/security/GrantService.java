package net.myconfig.service.api.security;

import java.util.EnumSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;

public interface GrantService {

	boolean hasUserFunction(String name, UserFunction fn);

	boolean hasAppFunction(int application, String name, AppFunction fn);

	boolean hasEnvFunction(int application, String name, String environment, EnvFunction fn);

	EnumSet<UserFunction> getUserFunctions(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	Ack appFunctionAdd(int application, String user, AppFunction fn);

	Ack appFunctionRemove(int application, String user, AppFunction fn);

	EnumSet<AppFunction> getAppFunctions(int application, String user);

	Ack envFunctionAdd(int application, String user, String environment, EnvFunction fn);

	Ack envFunctionRemove(int application, String user, String environment, EnvFunction fn);

	EnumSet<EnvFunction> getEnvFunctions(int application, String user, String environment);

}
