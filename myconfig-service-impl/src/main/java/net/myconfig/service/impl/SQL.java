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
	
	String ENVIRONMENTS = "select e.name " +
			"from environment e " + 
			"where e.application = :id " +
			"order by e.name";

	String ENVIRONMENT_CREATE = "insert into environment (application, name) values (:id, :name)";

	String ENVIRONMENT_DELETE = "delete from environment where application = :id and name = :name";

	String KEYS = "select k.name, k.description, count (vk.version) as versionNumber " +
			"from key k " +
			"left join version_key vk " +
			"on vk.application = k.application " + 
			"and vk.key = k.name " + 
			"where k.application = :id " +
			"group by k.name " +
			"order by k.name";

	String KEY_CREATE = "insert into key (application, name, description) values (:id, :name, :description)";

	String KEY_DELETE = "delete from key where application = :id and name = :name";

	String APPLICATION_EXISTS = "select id from application where name = :name";

	String VERSION_EXISTS = "select v.name " +
			"from version v, application a " +
			"where v.name = :name " +
			"and v.application = a.id " +
			"and a.name = :application";

	String ENVIRONMENT_EXISTS = "select e.name " +
			"from environment e, application a " +
			"where e.name = :name " +
			"and e.application = a.id " +
			"and a.name = :application";

}
