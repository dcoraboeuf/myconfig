package net.myconfig.web.rest;

import java.util.List;

import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationSummary;

public interface UIInterface {

	List<ApplicationSummary> applications();

	ApplicationSummary applicationCreate(String name);

	Ack applicationDelete(int id);

}
