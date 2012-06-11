package net.myconfig.service.api;

import java.util.List;

import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConfigurationSet;

public interface MyConfigService {

	String getKey(String application, String version, String environment, String key);

	String getVersion();

	ConfigurationSet getEnv(String application, String version, String environment);

	List<ApplicationSummary> getApplications();

	ApplicationSummary createApplication(String name);

}
