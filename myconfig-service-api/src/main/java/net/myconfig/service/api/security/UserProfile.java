package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;

public interface UserProfile {

	String getName();

	boolean hasUserFunction(UserFunction fn);

	boolean hasAppFunction(int application, AppFunction fn);

	boolean hasEnvFunction(int application, String environment, EnvFunction fn);

	boolean isAdmin();

	String getDisplayName();

	String getEmail();

}
