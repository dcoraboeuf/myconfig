package net.myconfig.service.db;

public interface SQL {

	String GET_KEY = "select c.value " +
			"from config c " +
			"where c.application = :application " +
			"and c.version = :version " +
			"and c.environment = :environment " +
			"and c.appkey = :appkey";
	
	String GET_ENV = "select c.appkey, k.description, c.value " +
			"from config c, appkey k " +
			"where c.application = :application " +
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
			"order by a.id";
	
	String APPLICATIONS_FOR_USER_RIGHTS = "select a.id, a.name from application a order by a.name";
	
	String APPLICATION_CREATE = "insert into application (id, name) values (:id, :name)";

	String APPLICATION_DELETE = "delete from application where id = :id";

	String APPLICATION_NAME = "select name from application where id = :id";

	String APPLICATION_ID = "select id from application where name = :name";
	
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
			"order by e.ordernb";
	
	String ENVIRONMENT_SUMMARIES = "select e.name, " +
			"(select COUNT(*) from version_key vk where vk.application = e.application) as keyCount, " +
			"(select COUNT(*) from config c where c.application = e.application and c.environment = e.name) as valueCount " +
			"from environment e " + 
			"where e.application = :application " +
			"order by e.ordernb";

	String ENVIRONMENT_CREATE = "insert into environment (application, name, ordernb) values (:id, :name, :ordernb)";
	String ENVIRONMENT_COUNT = "select count(*) from ENVIRONMENT WHERE APPLICATION = :id";

	String ENVIRONMENT_DELETE = "delete from environment where application = :id and name = :name";
	
	String KEY = "select * from appkey where application = :application and name = :appkey";

	String KEYS = "select * from appkey where application = :application order by name";

	String KEY_SUMMARIES = "select k.name, k.description, k.typeId, k.typeParam, COUNT(vk.version) as versionCount, " +
			"(select COUNT(*) from environment e where e.application = k.application) as environmentCount, " + 
			"(select COUNT(*) from config c where c.application = k.application and c.appkey  = k.name) as valueCount " +
			"from appkey k " +
			"left join version_key vk " +
			"on vk.application = k.application " + 
			"and vk.appkey = k.name " + 
			"where k.application = :application " +
			"group by k.name " +
			"order by k.name";

	String KEY_CREATE = "insert into appkey (application, name, description, typeid, typeparam) values (:id, :name, :description, :typeid, :typeparam)";
	
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
	
	String CONFIG_FOR_VERSION = "select c.* " +
			"from config c, environment e " +
			"where c.application = :application " +
			"and c.environment = e.name " +
			"and c.application = e.application " +
			"and c.version = :version " +
			"order by e.ordernb, c.appkey";
	
	String CONFIG_FOR_ENVIRONMENT = "select * " +
			"from config " +
			"where application = :application " +
			"and environment = :environment " +
			"order by version, appkey";
	
	String CONFIG_FOR_KEY = "select c.* " +
			"from config c, environment e " +
			"where c.application = :application " +
			"and c.application = e.application " +
			"and c.environment = e.name " +
			"and c.appkey = :appkey " +
			"order by c.version, e.ordernb";

	String CONFIG_REMOVE_VALUE = "delete from config where application = :application and version = :version and environment = :environment and appkey = :appkey";

	String CONFIG_INSERT_VALUE = "insert into config (application, version, environment, appkey, value) values (:application, :version, :environment, :appkey, :value)";

	String VERSION_PREVIOUS = "select name from version where application = :application and name < :version order by name desc";
	String VERSION_NEXT = "select name from version where application = :application and name > :version order by name asc";

	String ENVIRONMENT_PREVIOUS = "SELECT E.NAME FROM ENVIRONMENT E WHERE E.APPLICATION = :application AND E.ORDERNB < (SELECT EE.ORDERNB FROM ENVIRONMENT EE WHERE EE.APPLICATION = :application AND EE.NAME = :environment) ORDER BY E.ORDERNB DESC LIMIT 1";
	String ENVIRONMENT_NEXT = "SELECT E.NAME FROM ENVIRONMENT E WHERE E.APPLICATION = :application AND E.ORDERNB > (SELECT EE.ORDERNB FROM ENVIRONMENT EE WHERE EE.APPLICATION = :application AND EE.NAME = :environment) ORDER BY E.ORDERNB ASC LIMIT 1";

	String KEY_PREVIOUS = "select name from appkey  where application = :application and name < :appkey order by name desc";
	String KEY_NEXT = "select name from appkey where application = :application and name > :appkey order by name asc";

	String APPLICATION_EXISTS = "select id from application where name = :name";

	String VERSION_EXISTS = "select v.name " +
			"from version v " +
			"where v.name = :name " +
			"and v.application = :application";

	String KEY_EXISTS_BY_ID = "select name " +
			"from appkey " +
			"where name = :name " +
			"and application = :application";

	String ENVIRONMENT_EXISTS = "select e.name " +
			"from environment e " +
			"where e.name = :name " +
			"and e.application = :application";

	String MATRIX_EXISTS = "select version from version_key where application = :application and appkey = :appkey and version = :version";

	String CONFIGURATION_VALUE = "select value from configuration where name = :name";
	String CONFIGURATION_ADD = "insert into configuration (name, value) values (:name, :value)";
	String CONFIGURATION_UPDATE = "update configuration set value = :value where name = :name";

	String FUNCTIONS_USER = "select grantedfunction from usergrants where user = :user order by grantedfunction";
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
	
	String USER_MODE = "select mode from users where name = :name and verified = true and disabled = false";
	String USER = "select * from users where name = :name and password = :password and verified = true and disabled = false";
	String USER_SUMMARIES = "select name, displayname, email, admin, verified, disabled from users order by admin desc, name asc";
	String USER_COUNT = "select count(*) from users";
	String USER_CREATE = "insert into users (name, displayname, password, admin, email, verified, disabled) values (:name, :displayName, '', false, :email, false, false)";
	String USER_INIT = "insert into users (name, displayname, mode, password, admin, email, verified, disabled) values (" +
			"'admin', 'Administrator', 'builtin', " +
			"'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec'," +
			"true, '', true, false)";

	String USER_DELETE = "delete from users where name = :name and admin = false";
	String USER_FUNCTIONS_DELETE = "delete from usergrants where user = :name";
	String APP_FUNCTIONS_DELETE = "delete from appgrants where user = :name";
	String ENV_FUNCTIONS_DELETE = "delete from envgrants where user = :name";

	String TOKEN_SAVE = "insert into tokens (token, tokentype, tokenkey, creation) values (:token, :tokentype, :tokenkey, :creation)";
	String TOKEN_CHECK = "select creation from tokens where token = :token and tokentype = :tokentype and tokenkey = :tokenkey";
	String TOKEN_DELETE = "delete from tokens where tokentype = :tokentype and tokenkey = :tokenkey";
	String TOKEN_CLEANUP = "delete from tokens where creation < :creation";

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

	String ENVIRONMENT_GET_ORDER = "SELECT ORDERNB FROM ENVIRONMENT WHERE APPLICATION = :id AND NAME = :name";
	String ENVIRONMENT_SET_ORDER = "UPDATE ENVIRONMENT SET ORDERNB = :ordernb WHERE APPLICATION = :id AND NAME = :name";
	String ENVIRONMENT_REORDER_ABOVE = "UPDATE ENVIRONMENT SET ORDERNB = ORDERNB - 1 WHERE APPLICATION = :id AND ORDERNB > :ordernb";
}
