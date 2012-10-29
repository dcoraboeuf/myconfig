package net.myconfig.service.db;

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
	
	String APPLICATIONS_FOR_USER_RIGHTS = "select a.id, a.name from application a order by a.name";
	
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

	String CONFIGURATION_VALUE = "select value from configuration where name = :name";
	String CONFIGURATION_ADD = "insert into configuration (name, value) values (:name, :value)";
	String CONFIGURATION_UPDATE = "update configuration set value = :value where name = :name";

	String FUNCTIONS_USER = "select grantedfunction from usergrants where (user = :user or user = '*') order by grantedfunction";
	String FUNCTION_USER = "select grantedfunction from usergrants where (user = :user or user = '*') and grantedfunction = :grantedfunction";
	String FUNCTIONS_USER_ADD = "insert into usergrants (user, grantedfunction) values (:user, :grantedfunction)";
	String FUNCTIONS_USER_REMOVE = "delete from usergrants where user = :user and grantedfunction = :grantedfunction";
	
	String FUNCTION_APP	= "select grantedfunction from appgrants where (user = :user or user = '*') and application = :application and grantedfunction = :grantedfunction";
	String FUNCTION_APP_LIST_FOR_USER = "select grantedfunction from appgrants where user = :user and application = :application";
	String GRANT_APP_FUNCTION = "insert into appgrants (user, application, grantedfunction) values (:user, :application, :grantedfunction)";
	String UNGRANT_APP_FUNCTION = "delete from appgrants where user = :user and application = :application and grantedfunction = :grantedfunction";

	String FUNCTION_ENV	= "select grantedfunction from envgrants where (user = :user or user = '*') and application = :application and environment = :environment and grantedfunction = :grantedfunction";
	String FUNCTION_ENV_LIST_FOR_USER_AND_APPLICATION = "select grantedfunction from envgrants where user = :user and application = :application and environment = :environment";
	String GRANT_ENV_FUNCTION = "insert into envgrants (user, application, environment, grantedfunction) values (:user, :application, :environment, :grantedfunction)";
	String UNGRANT_ENV_FUNCTION = "delete from envgrants where user = :user and application = :application and environment = :environment and grantedfunction = :grantedfunction";
	
	String USER = "select * from users where name = :name and password = :password and verified = true and disabled = false";
	String USER_SUMMARIES = "select name, displayname, email, admin, verified, disabled from users order by admin desc, name asc";
	String USER_COUNT = "select count(*) from users";
	String USER_CREATE = "insert into users (name, displayname, password, admin, email, verified, disabled) values (:name, :displayName, '', false, :email, false, false)";
	String USER_INIT = "insert into users (name, displayname, password, admin, email, verified, disabled) values (" +
			"'admin', 'Administrator', " +
			"'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec'," +
			"true, '', true, false)";

	String USER_DELETE = "delete from users where name = :name and admin = false";
	String USER_FUNCTIONS_DELETE = "delete from usergrants where user = :name";
	String APP_FUNCTIONS_DELETE = "delete from appgrants where user = :name";
	String ENV_FUNCTIONS_DELETE = "delete from envgrants where user = :name";

	String TOKEN_SAVE = "insert into tokens (token, tokentype, tokenkey, creation) values (:token, :tokentype, :tokenkey, :creation)";
	String TOKEN_CHECK = "select creation from tokens where token = :token and tokentype = :tokentype and tokenkey = :tokenkey";
	String TOKEN_DELETE = "delete from tokens where tokentype = :tokentype and tokenkey = :tokenkey";

	String USER_CONFIRM = "update users set password = :password, verified = true where name = :user and disabled = false";

	String USER_EMAIL = "select email from users where name = :user";
	String USER_DISPLAY_NAME = "select displayName from users where name = :user";

	String USER_RESET = "update users set password = :password where name = :user";
	String USER_CHANGE_PASSWORD = "update users set password = :newpassword where name = :user and password = :password";
	String USER_UPDATE = "update users set displayName = :displayName, email = :email where name = :name and password = :password and verified = true and disabled = false";

	String USER_FORGOTTEN = "select name from users where email = :email and verified = true";

	String USER_DISABLE = "update users set disabled = true where name = :user and admin = false";

	String USER_ENABLE = "update users set disabled = false where name = :user";

	String USER_NAMES = "select name, displayName from users where verified = true and disabled = false and admin = false";
}
