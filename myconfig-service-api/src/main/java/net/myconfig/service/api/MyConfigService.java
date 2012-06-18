package net.myconfig.service.api;

import java.util.List;

import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConfigurationSet;

public interface MyConfigService {

	String getKey(String application, String version, String environment, String key);

	String getVersion();

	ConfigurationSet getEnv(String application, String version, String environment);

	List<ApplicationSummary> getApplications();

	ApplicationSummary createApplication(String name);

	Ack deleteApplication(int id);

	ApplicationConfiguration getApplicationConfiguration(int id);

	Ack createVersion(int id, String name);

	Ack deleteVersion(int id, String name);

	Ack createEnvironment(int id, String name);

	Ack deleteEnvironment(int id, String name);

	Ack deleteKey(int id, String name);

}
