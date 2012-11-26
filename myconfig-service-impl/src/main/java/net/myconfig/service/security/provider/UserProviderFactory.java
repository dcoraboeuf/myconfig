package net.myconfig.service.security.provider;

public interface UserProviderFactory {

	UserProvider getProvider(String mode);

	UserProvider getRequiredProvider(String mode);

}
