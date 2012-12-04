package net.myconfig.service.api.security;

public interface LDAPConfigurator {

	LDAPConfiguration loadConfiguration();

	void saveConfiguration(LDAPConfiguration configuration);

}
