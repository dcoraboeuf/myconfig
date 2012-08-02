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

	String APPLICATIONS = "select a.id, a.name, " +
			"(select COUNT(*) from version v where v.application = a.id) as versionCount, " + 
			"(select COUNT(*) from appkey k where k.application = a.id) as keyCount,  " +
			"(select COUNT(*) from environment e where e.application = a.id) as environmentCount, " +
			"(select COUNT(*) " +
			"	from environment e, version_key vk " +
			"	where e.application = a.id " +
			"	and vk.application = a.id) as configCount, " +
			"(select COUNT(*) from config where application = a.id) as valueCount " +
			"from application a  " +
			"order by a.name";
	
	String APPLICATION_CREATE = "insert into application (name) values (:name)";

	String APPLICATION_DELETE = "delete from application where id = :id";

	String APPLICATION_NAME = "select name from application where id = :id";
	
	String VERSIONS = "select * from version where application = :application order by name";
	
	String VERSION_SUMMARIES = "select v.name, " + 
			"(select COUNT(vk.appkey) from version_key vk where vk.application = v.application and vk.version = v.name) as keyCount, " +
			"(select COUNT(*) from config c where c.application = v.application and c.version = v.name) as valueCount, " +
			"(select COUNT(*) from environment e where e.application = v.application) as environmentCount " +
			"from version v " +
			"where v.application = :application " +
			"order by v.name";

	String VERSION_CREATE = "insert into version (application, name) values (:id, :name)";

	String VERSION_DELETE = "delete from version where application = :id and name = :name";
	
	String ENVIRONMENTS = "select e.name " +
			"from environment e " + 
			"where e.application = :application " +
			"order by e.name";
	
	String ENVIRONMENT_SUMMARIES = "select e.name, " +
			"(select COUNT(*) from version_key vk where vk.application = e.application) as keyCount, " +
			"(select COUNT(*) from config c where c.application = e.application and c.environment = e.name) as valueCount " +
			"from environment e " + 
			"where e.application = :application " +
			"order by e.name";

	String ENVIRONMENT_CREATE = "insert into environment (application, name) values (:id, :name)";

	String ENVIRONMENT_DELETE = "delete from environment where application = :id and name = :name";
	
	String KEY = "select * from appkey where application = :application and name = :appkey";

	String KEYS = "select * from appkey where application = :application order by name";

	String KEY_SUMMARIES = "select k.name, k.description, COUNT(vk.version) as versionCount, " +
			"(select COUNT(*) from environment e where e.application = k.application) as environmentCount, " + 
			"(select COUNT(*) from config c where c.application = k.application and c.appkey  = k.name) as valueCount " +
			"from appkey k " +
			"left join version_key vk " +
			"on vk.application = k.application " + 
			"and vk.appkey = k.name " + 
			"where k.application = :application " +
			"group by k.name " +
			"order by k.name";

	String KEY_CREATE = "insert into appkey (application, name, description) values (:id, :name, :description)";
	
	String KEY_UPDATE = "update appkey set description = :description where application = :application and name = :name";

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
	
	String VERSIONS_FOR_KEY = "select version from version_key where application = :application and appkey = :appkey order by version";
	
	String KEYS_FOR_ENVIRONMENT = "select k.* " +
			"from appkey k " +
			"where k.application = :application " +
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
	
	String CONFIG_FOR_ENVIRONMENT = "select * " +
			"from config " +
			"where application = :application " +
			"and environment = :environment " +
			"order by version, appkey";
	
	String CONFIG_FOR_KEY = "select * " +
			"from config " +
			"where application = :application " +
			"and appkey = :appkey " +
			"order by version, environment";

	String CONFIG_REMOVE_VALUE = "delete from config where application = :application and version = :version and environment = :environment and appkey = :appkey";

	String CONFIG_INSERT_VALUE = "insert into config (application, version, environment, appkey, value) values (:application, :version, :environment, :appkey, :value)";

	String VERSION_PREVIOUS = "select name from version where application = :application and name < :version order by name desc";
	String VERSION_NEXT = "select name from version where application = :application and name > :version order by name asc";

	String ENVIRONMENT_PREVIOUS = "select name from environment where application = :application and name < :environment order by name desc";
	String ENVIRONMENT_NEXT = "select name from environment where application = :application and name > :environment order by name asc";

	String KEY_PREVIOUS = "select name from appkey  where application = :application and name < :appkey order by name desc";
	String KEY_NEXT = "select name from appkey where application = :application and name > :appkey order by name asc";

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

	String ENVIRONMENT_EXISTS_BY_ID = "select e.name " +
			"from environment e " +
			"where e.name = :name " +
			"and e.application = :application";

	String CONFIGURATION_VALUE = "select value from configuration whre name = :name";
	
	String FUNCTIONS_USER = "select grantedfunction from usergrants where (user = :user or user = '*') order by grantedfunction";
	
	String USER = "select * from users where name = :name and password = :password";

}
