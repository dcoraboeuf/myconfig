package net.myconfig.service.audit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.junit.Test;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.Key;
import net.myconfig.service.impl.AbstractSecurityTest;

public class AuditIntegrationTest extends AbstractSecurityTest {

	
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
		// Data
		String[] envs = { "DEV", "PROD" };
		String[] versions = { "1", "2" };
		Key[] keys = { new Key("key1", "Key 1"), new Key("key2", "Key 2") };
		// Environment
		for (String env: envs) {
			myConfigService.createEnvironment(id, env);
		}
		// Keys
		for (Key key: keys) {
			myConfigService.createKey(id, key.getName(), key.getDescription());
		}
		// Versions
		for (String version: versions) {
			myConfigService.createVersion(id, version);
		}
		// Matrix
		for (Key key: keys) {
			for (String version: versions) {
				myConfigService.addKeyVersion(id, version, key.getName());
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
		assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
				" and category = 'APPLICATION' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment is null and version is null and appkey is null" +
				" and message is null", user, appName);
		// - environments
		for (String env: envs) {
			assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
				" and category = 'ENVIRONMENT' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment = '%s' and version is null and appkey is null" +
				" and message is null", user, id, env);
		}
		// - versions
		for (String version: versions) {
			assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
				" and category = 'VERSION' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment is null and version  = '%s' and appkey is null" +
				" and message is null", user, id, version);
		}
		// - keys
		for (Key key: keys) {
			assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
				" and category = 'KEY' and action = 'CREATE' and identifier is null" +
				" and application = '%s' and environment is null and version is null and appkey  = '%s'" +
				" and message = '%s'", user, id, key.getName(), key.getDescription());
		}
		// - matrix
		for (String version: versions) {
			for (Key key: keys) {
				assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
					" and category = 'MATRIX' and action = 'CREATE' and identifier is null" +
					" and application = '%s' and environment is null and version = '%s' and appkey  = '%s'" +
					" and message is null", user, id, version, key.getName());
			}
		}
		// -configuration
		for (String version: versions) {
			for (Key key: keys) {
				for (String env: envs) {
					assertRecordExists("select id from events where security = 'builtin' and user = '%s'" +
						" and category = 'CONFIG_VALUE' and action = 'UPDATE' and identifier is null" +
						" and application = '%s' and environment = '%s' and version = '%s' and appkey  = '%s'" +
						" and message is null", user, id, env, version, key.getName());
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
		assertTrue(ack.isSuccess());
		assertRecordExists("select id from events where security = 'builtin' and user = 'admin'" +
				" and category = 'APP_FUNCTION' and action = 'CREATE' and identifier = '%s'" +
				" and message = '%s -> %s'", id, user, AppFunction.app_matrix);
		ack = securityService.appFunctionRemove(id, user, AppFunction.app_matrix);
		assertTrue(ack.isSuccess());
		assertRecordExists("select id from events where security = 'builtin' and user = 'admin'" +
				" and category = 'APP_FUNCTION' and action = 'DELETE' and identifier = '%s'" +
				" and message = '%s -> %s'", id, user, AppFunction.app_matrix);
	}
}
