package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.EnvironmentAlreadyDefinedException;
import net.myconfig.service.exception.EnvironmentNotFoundException;
import net.myconfig.service.exception.KeyAlreadyDefinedException;
import net.myconfig.service.exception.KeyAlreadyInVersionException;
import net.myconfig.service.exception.KeyNotDefinedException;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.exception.VersionAlreadyDefinedException;
import net.myconfig.service.exception.VersionNotDefinedException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.service.model.EnvironmentSummary;
import net.myconfig.service.model.Key;
import net.myconfig.service.model.KeySummary;
import net.myconfig.service.model.KeyVersionConfiguration;
import net.myconfig.service.model.VersionConfiguration;
import net.myconfig.service.model.VersionSummary;
import net.myconfig.test.AbstractIntegrationTest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MyConfigServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService;
	
	@Test
	public void get_key_ok() {
		String value = myConfigService.getKey("myapp", "1.1", "UAT", "jdbc.user");
		assertEquals ("1.1 jdbc.user UAT", value);
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void get_key_not_found() {
		myConfigService.getKey("myapp", "1.1", "UAT", "jdbc.usr");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void get_key_noapp() {
		myConfigService.getKey("xxx", "1.1", "UAT", "jdbc.user");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void get_key_noversion() {
		myConfigService.getKey("myapp", "1.x", "UAT", "jdbc.user");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void get_key_noenv() {
		myConfigService.getKey("myapp", "1.1", "xxx", "jdbc.user");
	}
	
	@Test
	public void get_env () {
		ConfigurationSet set = myConfigService.getEnv("myapp", "1.1", "UAT");
		assertNotNull (set);
		List<ConfigurationValue> values = set.getValues();
		assertNotNull (values);
		assertEquals (2, values.size());
		{
			ConfigurationValue value = values.get(0);
			assertEquals ("jdbc.password", value.getKey());
			assertEquals ("Password used to connect to the database", value.getDescription());
			assertEquals ("1.1 jdbc.password UAT", value.getValue());
		}
		{
			ConfigurationValue value = values.get(1);
			assertEquals ("jdbc.user", value.getKey());
			assertEquals ("User used to connect to the database", value.getDescription());
			assertEquals ("1.1 jdbc.user UAT", value.getValue());
		}
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void get_env_no_app () {
		myConfigService.getEnv("myappxxx", "1.1", "UAT");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void get_env_no_version () {
		myConfigService.getEnv("myapp", "1.x", "UAT");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void get_env_no_environment () {
		myConfigService.getEnv("myapp", "1.1", "XXX");
	}
	
	@Test
	public void applications() {
		List<ApplicationSummary> applications = myConfigService.getApplications();
		assertNotNull (applications);
		{
			ApplicationSummary app = applications.get(0);
			assertEquals (2, app.getId());
			assertEquals ("anotherapp", app.getName());
			// TODO Assert stats
		}
		{
			ApplicationSummary app = applications.get(1);
			assertEquals (1, app.getId());
			assertEquals ("myapp", app.getName());
			// TODO Assert stats
		}
	}
	
	@Test
	public void applicationCreate () {
		ApplicationSummary summary = myConfigService.createApplication("test");
		assertNotNull (summary);
		assertEquals ("test", summary.getName());
		assertTrue (summary.getId() > 0);
		// TODO Assert stats
	}
	
	@Test(expected = ApplicationNameAlreadyDefinedException.class)
	public void applicationCreate_not_unique () {
		myConfigService.createApplication("test2");
		myConfigService.createApplication("test2");
	}

	@Test
	public void applicationDelete () {
		int id = myConfigService.createApplication("test3").getId();
		Ack ack = myConfigService.deleteApplication(id);
		assertNotNull (ack);
		assertTrue (ack.isSuccess());
	}

	@Test
	public void applicationDelete_cannot () {
		Ack ack = myConfigService.deleteApplication(-1);
		assertNotNull (ack);
		assertFalse (ack.isSuccess());
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void configuration_noapp () {
		myConfigService.getApplicationConfiguration(10);
	}
		
	@Test
	public void configuration () {
		ApplicationConfiguration app = myConfigService.getApplicationConfiguration(1);
		assertNotNull (app);
		assertEquals (1, app.getId());
		assertEquals ("myapp", app.getName());
		// Versions
		List<VersionSummary> versions = app.getVersionSummaryList();
		assertNotNull (versions);
		assertEquals (3, versions.size());
		assertVersionSummary ("1.0", 2, versions.get(0));
		assertVersionSummary ("1.1", 2, versions.get(1));
		assertVersionSummary ("1.2", 3, versions.get(2));
		// Environments
		List<EnvironmentSummary> environments = app.getEnvironmentSummaryList();
		assertNotNull (environments);
		assertEquals (4, environments.size());
		assertEnvironmentSummary ("ACC", environments.get(0));
		assertEnvironmentSummary ("DEV", environments.get(1));
		assertEnvironmentSummary ("PROD", environments.get(2));
		assertEnvironmentSummary ("UAT", environments.get(3));
		// Keys
		List<KeySummary> keys = app.getKeySummaryList();
		assertNotNull (keys);
		assertEquals (3, keys.size());
		assertKeySummary ("jdbc.password", "Password used to connect to the database", 3, keys.get(0));
		assertKeySummary ("jdbc.url", "URL used to connect to the database", 1, keys.get(1));
		assertKeySummary ("jdbc.user", "User used to connect to the database", 3, keys.get(2));
	}
	
	@Test
	public void version_create () throws DataSetException, SQLException {
		Ack ack = myConfigService.createVersion(1, "1.3");
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists ("select * from version where application = 1 and name = '1.3'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_create_noapp () {
		myConfigService.createVersion(10, "1.3");
	}
	
	@Test(expected = VersionAlreadyDefinedException.class)
	public void version_create_exists () {
		myConfigService.createVersion(1, "1.1");
	}
	
	@Test
	public void version_delete () throws DataSetException, SQLException {
		myConfigService.createVersion(1, "2.0");
		assertRecordExists("select * from version where application = 1 and name = '2.0'");
		Ack ack = myConfigService.deleteVersion(1, "2.0");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from version where application = 1 and name = '2.0'");
	}
	
	@Test
	public void version_delete_none () throws DataSetException, SQLException {
		Ack ack = myConfigService.deleteVersion(1, "2.1");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void version_delete_noapp () {
		myConfigService.deleteVersion(10, "1.0");
	}
	
	@Test
	public void environment_create () throws DataSetException, SQLException {
		Ack ack = myConfigService.createEnvironment(1, "TEST");
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists ("select * from environment where application = 1 and name = 'TEST'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void environment_create_noapp () {
		myConfigService.createEnvironment(10, "TEST");
	}
	
	@Test(expected = EnvironmentAlreadyDefinedException.class)
	public void environment_create_exists () {
		myConfigService.createEnvironment(1, "DEV");
	}
	
	@Test
	public void environment_delete () throws DataSetException, SQLException {
		myConfigService.createEnvironment(1, "TEST2");
		assertRecordExists("select * from environment where application = 1 and name = 'TEST2'");
		Ack ack = myConfigService.deleteEnvironment(1, "TEST2");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from environment where application = 1 and name = 'TEST2'");
	}
	
	@Test
	public void environment_delete_none () {
		Ack ack = myConfigService.deleteEnvironment(1, "TEST3");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void environment_delete_noapp () {
		myConfigService.deleteEnvironment(10, "TEST");
	}
	
	@Test
	public void key_create () throws DataSetException, SQLException {
		Ack ack = myConfigService.createKey(1, "key1", "Description for key 1");
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists("select * from key where application = 1 and name = 'key1' and description = 'Description for key 1'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void key_create_noapp () {
		myConfigService.createKey(10, "key2", "Description for key 2");
	}
	
	@Test(expected = KeyAlreadyDefinedException.class)
	public void key_create_exists () {
		myConfigService.createKey(1, "jdbc.user", "New description");
	}
	
	@Test
	public void key_delete () throws DataSetException, SQLException {
		myConfigService.createKey(1, "key3", "Description for key 3");
		assertRecordExists("select * from key where application = 1 and name = 'key3' and description = 'Description for key 3'");
		Ack ack = myConfigService.deleteKey(1, "key3");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from key where application = 1 and name = 'key3' and description = 'Description for key 3'");
	}
	
	@Test
	public void key_delete_none () {
		Ack ack = myConfigService.deleteKey(1, "key4");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void key_delete_noapp () {
		myConfigService.deleteKey(10, "key5");
	}
	
	@Test
	public void version_configuration () throws JsonGenerationException, JsonMappingException, IOException {
		KeyVersionConfiguration configuration = myConfigService.keyVersionConfiguration(1);
		assertNotNull (configuration);
		assertJSONEquals (
				new KeyVersionConfiguration(1, "myapp",
					Arrays.asList(
							new VersionConfiguration(
									"1.0",
									Arrays.asList("jdbc.password", "jdbc.user")),
							new VersionConfiguration(
									"1.1",
									Arrays.asList("jdbc.password", "jdbc.user")),
							new VersionConfiguration(
									"1.2",
									Arrays.asList("jdbc.password", "jdbc.url", "jdbc.user"))),
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database"),
							new Key("jdbc.url", "URL used to connect to the database"),
							new Key("jdbc.user", "User used to connect to the database"))
					),
				configuration);
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_configuration_no_app () {
		myConfigService.keyVersionConfiguration(-1);
	}
	
	@Test
	public void version_key_add () throws DataSetException, SQLException {
		assertRecordNotExists("select * from version_key where application = 1 and version = '1.1' and key = 'jdbc.url'");
		Ack ack = myConfigService.addKeyVersion(1, "1.1", "jdbc.url");
		assertTrue (ack.isSuccess());
		assertRecordExists("select * from version_key where application = 1 and version = '1.1' and key = 'jdbc.url'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_key_add_noapp () throws DataSetException, SQLException {
		myConfigService.addKeyVersion(-1, "1.1", "jdbc.url");
	}
	
	@Test(expected = VersionNotDefinedException.class)
	public void version_key_add_noversion () throws DataSetException, SQLException {
		myConfigService.addKeyVersion(1, "1.X", "jdbc.url");
	}
	
	@Test(expected = KeyNotDefinedException.class)
	public void version_key_add_nokey () throws DataSetException, SQLException {
		myConfigService.addKeyVersion(1, "1.0", "jdbc.xxx");
	}
	
	@Test(expected = KeyAlreadyInVersionException.class)
	public void version_key_add_duplicate () throws DataSetException, SQLException {
		assertRecordExists("select * from version_key where application = 1 and version = '1.2' and key = 'jdbc.url'");
		myConfigService.addKeyVersion(1, "1.2", "jdbc.url");
	}
	
	@Test
	public void version_key_remove () throws DataSetException, SQLException {
		assertRecordExists("select * from version_key where application = 1 and version = '1.0' and key = 'jdbc.password'");
		Ack ack = myConfigService.removeKeyVersion(1, "1.0", "jdbc.password");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from version_key where application = 1 and version = '1.0' and key = 'jdbc.password'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_key_remove_noapp () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion(-1, "1.1", "jdbc.url");
	}
	
	@Test(expected = VersionNotDefinedException.class)
	public void version_key_remove_noversion () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion(1, "1.X", "jdbc.url");
	}
	
	@Test(expected = KeyNotDefinedException.class)
	public void version_key_remove_nokey () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion(1, "1.0", "jdbc.xxx");
	}
	
	@Test
	public void version_key_remove_none () throws DataSetException, SQLException {
		assertRecordNotExists("select * from version_key where application = 1 and version = '1.1' and key = 'jdbc.url'");
		Ack ack = myConfigService.removeKeyVersion(1, "1.1", "jdbc.url");
		assertFalse (ack.isSuccess());
	}
	
	private <T> void assertJSONEquals (T a, T b) throws JsonGenerationException, JsonMappingException, IOException {
		String ja = toJSON (a);
		String jb = toJSON (b);
		assertEquals (ja, jb);
	}
	
	private String toJSON (Object o) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
		return writer.writeValueAsString(o);
	}

	private void assertKeySummary(String name, String description, int versionNumber, KeySummary keySummary) {
		assertNotNull (keySummary);
		assertEquals (name, keySummary.getName());
		assertEquals (description, keySummary.getDescription());
		assertEquals (versionNumber, keySummary.getVersionNumber());
	}

	private void assertEnvironmentSummary(String name,
			EnvironmentSummary environmentSummary) {
		assertNotNull(environmentSummary);
		assertEquals (name, environmentSummary.getName());		
	}

	private void assertVersionSummary(String name, int keyNumber,
			VersionSummary versionSummary) {
		assertNotNull (versionSummary);
		assertEquals (name, versionSummary.getName());
		assertEquals (keyNumber, versionSummary.getKeyNumber());
	}

}
