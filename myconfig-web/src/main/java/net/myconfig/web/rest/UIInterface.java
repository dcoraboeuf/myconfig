package net.myconfig.web.rest;

import java.util.List;

import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.MatrixConfiguration;

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

	MatrixConfiguration keyVersionConfiguration(int id);

	Ack keyVersionAdd(int application, String version, String key);

	Ack keyVersionRemove(int application, String version, String key);

}
