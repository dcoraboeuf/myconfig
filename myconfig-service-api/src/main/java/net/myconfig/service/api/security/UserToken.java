package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;

public interface UserToken {

	User getUser();

	String getName();

	boolean hasUserFunction(UserFunction fn);

	boolean hasAppFunction(int application, AppFunction fn);

	boolean isAdmin();

}
