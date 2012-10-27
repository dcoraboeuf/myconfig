package net.myconfig.core;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.core.model.VersionConfiguration;

public interface MyConfigInterface {
	
	String version();

	ApplicationSummaries applications();

	ApplicationSummary applicationCreate(String name);

	Ack applicationDelete(int id);

	ApplicationConfiguration applicationConfiguration(int id);

	Ack versionCreate(int id, String name);

	Ack versionDelete(int id, String name);

	Ack environmentCreate(int id, String name);

	Ack environmentDelete(int id, String name);

	Ack keyDelete(int id, String name);

	Ack keyCreate(int id, String name, String description);

	Ack keyUpdate(int id, String name, String description);

	MatrixConfiguration keyVersionConfiguration(int id);

	Ack keyVersionAdd(int application, String version, String key);

	Ack keyVersionRemove(int application, String version, String key);

	VersionConfiguration versionConfiguration(int application, String version);

	EnvironmentConfiguration environmentConfiguration(int application, String environment);

	KeyConfiguration keyConfiguration(int application, String key);

	Ack updateConfiguration(int application, ConfigurationUpdates updates);

	UserSummaries users();

	Ack userCreate(String name, String displayName, String email);

	Ack userDelete(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	Ack appFunctionAdd(String user, int application, AppFunction fn);

	Ack appFunctionRemove(String user, int application, AppFunction fn);

	Ack setSecurityMode(String mode);

	Ack userConfirm(String name, String token, String password);
	
	ApplicationUsers applicationUsers(int application);

	EnvironmentUsers environmentUsers(int application, String environment);

	Ack envFunctionAdd(String user, int application, String environment, EnvFunction fn);

	Ack envFunctionRemove(String user, int application, String environment, EnvFunction fn);

}
