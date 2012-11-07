package net.myconfig.service.api;

import java.util.List;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserApplicationRights;
import net.myconfig.core.model.VersionConfiguration;
import net.sf.jstring.Localizable;

public interface MyConfigService {

	String getKey(String application, String version, String environment, String key);

	String getVersion();

	ConfigurationSet getEnv(String application, String version, String environment);

	ApplicationSummaries getApplications();

	ApplicationSummary createApplication(String id, String name);

	Ack deleteApplication(String id);

	ApplicationConfiguration getApplicationConfiguration(String id);

	Ack createVersion(String id, String name);

	Ack deleteVersion(String id, String name);

	Ack createEnvironment(String id, String name);

	Ack deleteEnvironment(String id, String name);

	Ack deleteKey(String id, String name);

	Ack createKey(String id, String name, String description, String typeId, String typeParam);

	MatrixConfiguration keyVersionConfiguration(String id);

	Ack addKeyVersion(String application, String version, String key);

	Ack removeKeyVersion(String application, String version, String key);

	VersionConfiguration getVersionConfiguration(String application, String version);

	EnvironmentConfiguration getEnvironmentConfiguration(String application, String environment);
	
	KeyConfiguration getKeyConfiguration(String application, String key);

	Ack updateConfiguration(String application, ConfigurationUpdates updates);

	Ack updateKey(String application, String name, String description);
	
	List<UserApplicationRights> getUserApplicationRights (String user);

	ApplicationUsers getApplicationUsers(String application);

	EnvironmentUsers getEnvironmentUsers(String application, String environment);

	Localizable validateTypeParameter(String typeId, String parameter);

	Localizable validateTypeValue(String typeId, String parameter, String value);

}
