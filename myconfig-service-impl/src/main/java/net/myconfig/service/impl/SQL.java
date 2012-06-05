package net.myconfig.service.impl;

public interface SQL {

	String GET_KEY = "select c.value from config c, application a where a.name = :application and a.id = c.application and c.version = :version and c.environment = :environment and c.key = :key";

}
