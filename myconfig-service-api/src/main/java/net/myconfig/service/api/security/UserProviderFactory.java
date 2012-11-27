package net.myconfig.service.api.security;

import java.util.List;

public interface UserProviderFactory {

	UserProvider getProvider(String mode);

	UserProvider getRequiredProvider(String mode);

	List<UserProvider> getProviders();

	List<UserProvider> getEnabledProviders();

}
