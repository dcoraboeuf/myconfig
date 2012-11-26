package net.myconfig.service.security.provider;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
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
	public UserProvider getProvider(String mode) {
		return userProviders.get(mode);
	}

}
