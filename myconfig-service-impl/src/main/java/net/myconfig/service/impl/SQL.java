package net.myconfig.service.impl;

public interface SQL {

	String GET_KEY = "select c.value " +
			"from config c, application a " +
			"where a.name = :application " +
			"and a.id = c.application " +
			"and c.version = :version " +
			"and c.environment = :environment " +
			"and c.appkey = :appkey";
	
	String GET_ENV = "select c.appkey, k.description, c.value " +
			"from config c, application a, appkey k " +
			"where a.name = :application and a.id = c.application " +
			"and k.application = c.application and k.name = c.appkey " +
			"and c.version = :version " +
			"and c.environment = :environment " +
			"order by c.appkey";

	String APPLICATIONS = "select a.id, a.name from application a order by a.name";
	
	String APPLICATION_CREATE = "insert into application (name) values (:name)";

	String APPLICATION_DELETE = "delete from application where id = :id";

	String APPLICATION_NAME = "select name from application where id = :id";
	
	String VERSIONS = "select * from version where application = :application order by name";
	
	String VERSION_SUMMARIES = "select v.name, COUNT(vk.appkey) as keyNumber " +
			"from version v " +
			"left join version_key vk " +
			"on vk.application = v.application " + 
			"and vk.version = v.name " + 
			"where v.application = :id " +
			"group by v.name " +
			"order by v.name";

	String VERSION_CREATE = "insert into version (application, name) values (:id, :name)";

	String VERSION_DELETE = "delete from version where application = :id and name = :name";
	
	String ENVIRONMENT_SUMMARIES = "select e.name " +
			"from environment e " + 
			"where e.application = :id " +
			"order by e.name";

	String ENVIRONMENT_CREATE = "insert into environment (application, name) values (:id, :name)";

	String ENVIRONMENT_DELETE = "delete from environment where application = :id and name = :name";

	String KEYS = "select * from appkey where application = :application order by name";

	String KEY_SUMMARIES = "select k.name, k.description, COUNT(vk.version) as versionNumber " +
			"from appkey k " +
			"left join version_key vk " +
			"on vk.application = k.application " + 
			"and vk.appkey = k.name " + 
			"where k.application = :id " +
			"group by k.name " +
			"order by k.name";

	String KEY_CREATE = "insert into appkey (application, name, description) values (:id, :name, :description)";

	String KEY_DELETE = "delete from appkey where application = :id and name = :name";
	
	String VERSION_KEYS = "select appkey " +
			"from version_key " +
			"where application = :application " +
			"and version = :version " +
			"order by appkey";
	
	String KEYS_FOR_VERSION = "select k.* " +
			"from version_key vk, appkey k " +
			"where vk.application = :application " +
			"and vk.version = :version " +
			"and k.application = vk.application " +
			"and k.name = vk.appkey " +
			"order by k.name";

	String VERSION_KEY_ADD = "insert into version_key (application, version, appkey) " +
			"values (:application, :version, :appkey)";

	String VERSION_KEY_REMOVE = "delete from version_key " +
			"where application = :application and version = :version and appkey = :appkey";
	
	String CONFIG_FOR_VERSION = "select * " +
			"from config " +
			"where application = :application " +
			"and version = :version " +
			"order by environment, appkey";

	String VERSION_PREVIOUS = "select name from version where application = :application and name < :version order by name desc";
	String VERSION_NEXT = "select name from version where application = :application and name > :version order by name asc";

	String APPLICATION_EXISTS = "select id from application where name = :name";

	String VERSION_EXISTS_BY_ID = "select v.name " +
			"from version v " +
			"where v.name = :name " +
			"and v.application = :application";

	String VERSION_EXISTS = "select v.name " +
			"from version v, application a " +
			"where v.name = :name " +
			"and v.application = a.id " +
			"and a.name = :application";

	String KEY_EXISTS_BY_ID = "select name " +
			"from appkey " +
			"where name = :name " +
			"and application = :application";

	String ENVIRONMENT_EXISTS = "select e.name " +
			"from environment e, application a " +
			"where e.name = :name " +
			"and e.application = a.id " +
			"and a.name = :application";

}
