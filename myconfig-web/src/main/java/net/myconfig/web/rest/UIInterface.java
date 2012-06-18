package net.myconfig.web.rest;

import java.util.List;

import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;

public interface UIInterface {

	List<ApplicationSummary> applications();

	ApplicationSummary applicationCreate(String name);

	Ack applicationDelete(int id);

	ApplicationConfiguration applicationConfiguration(int id);

	Ack versionCreate(int id, String name);

	Ack versionDelete(int id, String name);

	Ack environmentCreate(int id, String name);

	Ack environmentDelete(int id, String name);

}
