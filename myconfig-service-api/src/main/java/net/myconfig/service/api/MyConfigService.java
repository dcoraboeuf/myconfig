package net.myconfig.service.api;

import java.util.List;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserApplicationRights;
import net.myconfig.core.model.VersionConfiguration;

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

	Ack createKey(int id, String name, String description);

	MatrixConfiguration keyVersionConfiguration(int id);

	Ack addKeyVersion(int application, String version, String key);

	Ack removeKeyVersion(int application, String version, String key);

	VersionConfiguration getVersionConfiguration(int application, String version);

	EnvironmentConfiguration getEnvironmentConfiguration(int application, String environment);
	
	KeyConfiguration getKeyConfiguration(int application, String key);

	Ack updateConfiguration(int application, ConfigurationUpdates updates);

	Ack updateKey(int application, String name, String description);
	
	List<UserApplicationRights> getUserApplicationRights (String user);

}
