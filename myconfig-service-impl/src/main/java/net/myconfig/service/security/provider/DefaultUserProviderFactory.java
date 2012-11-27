package net.myconfig.service.security.provider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.myconfig.service.api.security.UserProvider;
import net.myconfig.service.api.security.UserProviderFactory;
import net.myconfig.service.exception.UserProviderModeNotDefinedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

@Component
public class DefaultUserProviderFactory implements UserProviderFactory {

	private final Map<String, UserProvider> userProviders;

	@Autowired
	public DefaultUserProviderFactory(Collection<UserProvider> userProviders) {
		this.userProviders = Maps.uniqueIndex(userProviders, new Function<UserProvider, String>() {
			@Override
			public String apply(UserProvider provider) {
				return provider.getId();
			}
		});
	}
	
	@Override
	public List<UserProvider> getProviders() {
		return ImmutableList.copyOf(userProviders.values());
	}
	
	@Override
	public List<UserProvider> getEnabledProviders() {
		return ImmutableList.copyOf(Iterables.filter(getProviders(), new Predicate<UserProvider>() {
			@Override
			public boolean apply (UserProvider userProvider) {
				return userProvider.isEnabled();
			}
		}));
	}

	@Override
	public UserProvider getProvider(String mode) {
		return userProviders.get(mode);
	}
	
	@Override
	public UserProvider getRequiredProvider(String mode) {
		UserProvider p = getProvider(mode);
		if (p != null) {
			return p;
		} else {
			throw new UserProviderModeNotDefinedException(mode);
		}
	}

}
