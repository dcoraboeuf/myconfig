package net.myconfig.service.audit

import java.sql.SQLException

import net.myconfig.core.AppFunction
import net.myconfig.core.EnvFunction
import net.myconfig.core.UserFunction
import net.myconfig.core.model.Ack
import net.myconfig.core.model.ConfigurationUpdate
import net.myconfig.core.model.ConfigurationUpdates
import net.myconfig.service.impl.AbstractSecurityTest

import org.dbunit.DatabaseUnitException
import org.dbunit.dataset.DataSetException
import org.junit.Test

class AuditIntegrationTest extends AbstractSecurityTest {

	@Test
	public void createApplication_admin() throws DataSetException, SQLException {
		asAdmin();
		String appName = appName();
		myConfigService.createApplication(appName);
		assertRecordExists("select id from events where security = 'builtin' and user = 'admin'" +
				" and category = 'APPLICATION' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment is null and version is null and appkey is null" +
				" and message is null", appName);
	}

	@Test
	public void createApplication_user() throws DataSetException, SQLException {
		String user = asUser().grant(UserFunction.app_create).getName();
		String appName = appName();
		myConfigService.createApplication(appName);
		assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
				" and category = 'APPLICATION' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment is null and version is null and appkey is null" +
				" and message is null", user, appName);
	}

	@Test
	public void configuration() throws SQLException, IOException, DatabaseUnitException {
		// User
		String user = asUser().grant(UserFunction.app_create).getName();
		// Application
		String appName = appName();
		int id = myConfigService.createApplication(appName).getId();
		// Environment
		myConfigService.createEnvironment(id, "DEV");
		myConfigService.createEnvironment(id, "PROD");
		// Keys
		myConfigService.createKey(id, "key1", "Key 1");
		myConfigService.createKey(id, "key2", "Key 2");
		// Versions
		myConfigService.createVersion(id, "1");
		myConfigService.createVersion(id, "2");
		// Matrix
		["1", "2"].each {
			version ->
			["key1", "key2"].each {
				key ->
				myConfigService.addKeyVersion(id, version, key);
			}
		}
		// Configuration
		ConfigurationUpdates updates = new ConfigurationUpdates(
				Arrays.asList(
						new ConfigurationUpdate("DEV", "1", "key1", "DEV 1 key1"),
						new ConfigurationUpdate("DEV", "1", "key2", "DEV 1 key2"),
						new ConfigurationUpdate("DEV", "2", "key1", "DEV 2 key1"),
						new ConfigurationUpdate("DEV", "2", "key2", "DEV 2 key2"),
						new ConfigurationUpdate("PROD", "1", "key1", "PROD 1 key1"),
						new ConfigurationUpdate("PROD", "1", "key2", "PROD 1 key2"),
						new ConfigurationUpdate("PROD", "2", "key1", "PROD 2 key1"),
						new ConfigurationUpdate("PROD", "2", "key2", "PROD 2 key2")
						));
		myConfigService.updateConfiguration(id, updates);
		// Assertions
		// - application
		assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
				" and category = 'APPLICATION' and action = 'CREATE' and identifier is null" +
				" and application = '${appName}' and environment is null and version is null and appkey is null" +
				" and message is null");
		// - environments
		["DEV", "PROD"].each {
			assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
				" and category = 'ENVIRONMENT' and action = 'CREATE' and identifier is null" +
				" and application = '${id}' and environment = '${it}' and version is null and appkey is null" +
				" and message is null");
		}
		// - versions
		["1", "2"].each {
			assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
				" and category = 'VERSION' and action = 'CREATE' and identifier is null" +
				" and application = '${id}' and environment is null and version  = '${it}' and appkey is null" +
				" and message is null");
		}
		// - keys
		[[name:"key1",description:"Key 1"], [name:"key2",description:"Key 2"]].each {
			assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
				" and category = 'KEY' and action = 'CREATE' and identifier is null" +
				" and application = '${id}' and environment is null and version is null and appkey  = '${it.name}'" +
				" and message = '${it.description}'");
		}
		// - matrix
		["1", "2"].each {
			version ->
			["key1", "key2"].each {
				key ->
				assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
					" and category = 'MATRIX' and action = 'CREATE' and identifier is null" +
					" and application = '${id}' and environment is null and version = '${version}' and appkey  = '${key}'" +
					" and message is null");
			}
		}
		// -configuration
		["1", "2"].each {
			version ->
			["key1", "key2"].each {
				key ->
				["DEV", "PROD"].each {
					env ->
					assertRecordExists("select id from events where security = 'builtin' and user = '${user}'" +
						" and category = 'CONFIG_VALUE' and action = 'UPDATE' and identifier is null" +
						" and application = '${id}' and environment = '${env}' and version = '${version}' and appkey  = '${key}'" +
						" and message is null");
				}
			}
		}
	}

	@Test
	public void appFunctions() throws SQLException, DataSetException {
		asAdmin();
		int id = myConfigService.createApplication(appName()).getId();
		String user = createUser();
		Ack ack = securityService.appFunctionAdd(id, user, AppFunction.app_matrix);
		assert ack.isSuccess()
		assertRecordExists("select id from events where security = 'builtin' and user = 'admin'" +
				" and category = 'APP_FUNCTION' and action = 'CREATE' and application = '%s'" +
				" and message = '%s -> %s'", id, user, AppFunction.app_matrix);
		ack = securityService.appFunctionRemove(id, user, AppFunction.app_matrix);
		assert ack.isSuccess()
		assertRecordExists("select id from events where security = 'builtin' and user = 'admin'" +
				" and category = 'APP_FUNCTION' and action = 'DELETE' and application = '%s'" +
				" and message = '%s -> %s'", id, user, AppFunction.app_matrix);
	}

	@Test
	public void envFunctions() throws SQLException, DataSetException {
		asAdmin();
		int id = myConfigService.createApplication(appName()).getId();
		myConfigService.createEnvironment(id, "TEST")
		String user = createUser();
		Ack ack = securityService.envFunctionAdd(id, user, "TEST", EnvFunction.env_view);
		assert ack.isSuccess()
		assertRecordExists("""select id from events where security = 'builtin' and user = 'admin'
				and category = 'ENV_FUNCTION' and action = 'CREATE' and application = '%s'
				and environment = 'TEST'
				and message = '%s -> %s'""", id, user, EnvFunction.env_view);
		ack = securityService.envFunctionRemove(id, user, "TEST", EnvFunction.env_view);
		assert ack.isSuccess()
		assertRecordExists("""select id from events where security = 'builtin' and user = 'admin'
				and category = 'ENV_FUNCTION' and action = 'DELETE' and application = '%s'
				and environment = 'TEST'
				and message = '%s -> %s'""", id, user, EnvFunction.env_view);
	}
}