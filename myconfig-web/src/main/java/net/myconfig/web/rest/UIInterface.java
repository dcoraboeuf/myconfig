package net.myconfig.web.rest;

import java.util.List;

import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.EnvironmentConfiguration;
import net.myconfig.service.model.KeyConfiguration;
import net.myconfig.service.model.MatrixConfiguration;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.model.VersionConfiguration;
import net.myconfig.service.model.ConfigurationUpdates;

public interface UIInterface {

	List<ApplicationSummary> applications();

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

	List<UserSummary> users();

	Ack userCreate(String name);

}
