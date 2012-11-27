package net.myconfig.service.api.security;

public interface UserProviderFactory {

	UserProvider getProvider(String mode);

	UserProvider getRequiredProvider(String mode);

}
