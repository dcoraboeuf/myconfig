package net.myconfig.service.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserToken;

public class UserTokenImpl implements UserToken {

	private final User user;
	private final Set<UserFunction> userFunctions;
	private final Map<Integer, Set<AppFunction>> appFunctions;

	public UserTokenImpl(User user, Collection<UserFunction> userFunctions, Map<Integer, Set<AppFunction>> appFunctions) {
		this.user = user;
		this.userFunctions = ImmutableSet.copyOf(userFunctions);
		this.appFunctions = ImmutableMap.copyOf(appFunctions);
	}

	@Override
	public User getUser() {
		return user;
	}
	
	@Override
	public String getName() {
		return user.getName();
	}
	
	@Override
	public boolean isAdmin() {
		return user.isAdmin();
	}

	@Override
	public boolean hasUserFunction(UserFunction fn) {
		if (user.isAdmin()) {
			return true;
		} else {
			return userFunctions.contains(fn);
		}
	}

	@Override
	public boolean hasAppFunction(int application, AppFunction fn) {
		if (user.isAdmin()) {
			return true;
		} else {
			Set<AppFunction> set = appFunctions.get(application);
			return set != null && set.contains(fn);
		}
	}

}
