package net.myconfig.service.api;

import net.myconfig.service.model.ConfigurationSet;

public interface MyConfigService {

	String getKey(String application, String version, String environment, String key);

	String getVersion();

	ConfigurationSet getEnv(String application, String version, String environment);

}
