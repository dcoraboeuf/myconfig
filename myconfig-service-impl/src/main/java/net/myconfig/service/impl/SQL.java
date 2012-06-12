package net.myconfig.service.impl;

public interface SQL {

	String GET_KEY = "select c.value from config c, application a where a.name = :application and a.id = c.application and c.version = :version and c.environment = :environment and c.key = :key";
	
	String GET_ENV = "select c.key, k.description, c.value " +
			"from config c, application a, key k " +
			"where a.name = :application and a.id = c.application " +
			"and k.application = c.application and k.name = c.key " +
			"and c.version = :version " +
			"and c.environment = :environment " +
			"order by c.key";

	String APPLICATIONS = "select a.id, a.name from application a order by a.name";
	
	String APPLICATION_CREATE = "insert into application (name) values (:name)";

	String APPLICATION_DELETE = "delete from application where id = :id";

	String APPLICATION_NAME = "select name from application where id = :id";
	
	String VERSIONS = "select v.name, count (vk.key) as keyNumber " +
			"from version v " +
			"left join version_key vk " +
			"on vk.application = v.application " + 
			"and vk.version = v.name " + 
			"where v.application = :id " +
			"group by v.name " +
			"order by v.name";

	String VERSION_CREATE = "insert into version (application, name) values (:id, :name)";

	String VERSION_DELETE = "delete from version where application = :id and name = :name";

}
