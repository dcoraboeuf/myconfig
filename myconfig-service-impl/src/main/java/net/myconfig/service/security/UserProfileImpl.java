package net.myconfig.service.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserProfile;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class UserProfileImpl implements UserProfile {

	private final User user;
	private final Set<UserFunction> userFunctions;
	private final Map<Integer, Set<AppFunction>> appFunctions;

	public UserProfileImpl(User user, Collection<UserFunction> userFunctions, Map<Integer, Set<AppFunction>> appFunctions) {
		this.user = user;
		this.userFunctions = ImmutableSet.copyOf(userFunctions);
		this.appFunctions = ImmutableMap.copyOf(appFunctions);
	}
	
	@Override
	public String getName() {
		return user.getName();
	}
	
	// TODO Uses a display name
	@Override
	public String getDisplayName() {
		return getName();
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
			Set<AppFunction> set = appFunctions.get(application);
			return set != null && set.contains(fn);
		}
	}
	
	@Override
	public boolean hasEnvFunction(int application, String environment, EnvFunction fn) {
		// TODO Auto-generated method stub
		return false;
	}

}
