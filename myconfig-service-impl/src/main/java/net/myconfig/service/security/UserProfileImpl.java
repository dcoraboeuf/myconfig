package net.myconfig.service.security;

import java.util.EnumSet;
import java.util.Set;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserProfile;

import com.google.common.collect.ImmutableSet;

public class UserProfileImpl implements UserProfile {

	private final User user;
	private final EnumSet<UserFunction> userFunctions;
	private final Set<AppFunctionKey> appFunctions;
	private final Set<EnvFunctionKey> envFunctions;

	public UserProfileImpl(User user, EnumSet<UserFunction> userFunctions, Set<AppFunctionKey> appFunctions, Set<EnvFunctionKey> envFunctions) {
		this.user = user;
		this.userFunctions = userFunctions;
		this.appFunctions = ImmutableSet.copyOf(appFunctions);
		this.envFunctions = ImmutableSet.copyOf(envFunctions);
	}

	@Override
	public String getName() {
		return user.getName();
	}

	@Override
	public String getDisplayName() {
		return user.getDisplayName();
	}

	@Override
	public boolean isAdmin() {
		return user.isAdmin();
	}

	@Override
	public boolean hasUserFunction(UserFunction fn) {
		if (isAdmin()) {
			return true;
		} else {
			return userFunctions.contains(fn);
		}
	}

	@Override
	public boolean hasAppFunction(int application, AppFunction fn) {
		if (isAdmin()) {
			return true;
		} else {
			return appFunctions.contains(new AppFunctionKey(application, fn));
		}
	}

	@Override
	public boolean hasEnvFunction(int application, String environment, EnvFunction fn) {
		if (isAdmin()) {
			return true;
		} else {
			return envFunctions.contains(new EnvFunctionKey(application, environment, fn));
		}
	}

}
