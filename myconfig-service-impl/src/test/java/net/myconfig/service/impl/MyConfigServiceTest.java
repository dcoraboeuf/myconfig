package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConditionalValue;
import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.ConfigurationValue;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.EnvironmentSummary;
import net.myconfig.core.model.IndexedValues;
import net.myconfig.core.model.Key;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.KeySummary;
import net.myconfig.core.model.MatrixConfiguration;
import net.myconfig.core.model.MatrixVersionConfiguration;
import net.myconfig.core.model.Version;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.core.model.VersionSummary;
import net.myconfig.core.utils.MapBuilder;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.exception.ApplicationIDAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.EnvironmentAlreadyDefinedException;
import net.myconfig.service.exception.EnvironmentNotFoundException;
import net.myconfig.service.exception.KeyAlreadyDefinedException;
import net.myconfig.service.exception.KeyAlreadyInVersionException;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.exception.MatrixNotFoundException;
import net.myconfig.service.exception.ValidationException;
import net.myconfig.service.exception.VersionAlreadyDefinedException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.test.AbstractIntegrationTest;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.dbunit.dataset.DataSetException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netbeetle.jackson.ObjectMapperFactory;

public class MyConfigServiceTest extends AbstractIntegrationTest {

	private static final String APP = "APP";

	@Autowired
	private MyConfigService myConfigService;
	
	@Autowired
	private SecurityService securityService;
	
	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private Strings strings;
	
	/**
	 * Makes sure to work without security enabled
	 */
	@Before
	public void securitySetup() {
		securityService.setSecurityMode("none");
	}
	
	@Test
	public void version() {
		assertEquals (appVersion, myConfigService.getVersion());
	}
	
	@Test
	public void get_key_ok() {
		String value = myConfigService.getKey(APP, "1.1", "UAT", "jdbc.user");
		assertEquals ("1.1 jdbc.user UAT", value);
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void get_key_not_found() {
		myConfigService.getKey(APP, "1.1", "UAT", "jdbc.usr");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void get_key_noapp() {
		myConfigService.getKey("xxx", "1.1", "UAT", "jdbc.user");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void get_key_noversion() {
		myConfigService.getKey(APP, "1.x", "UAT", "jdbc.user");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void get_key_noenv() {
		myConfigService.getKey(APP, "1.1", "xxx", "jdbc.user");
	}
	
	@Test
	public void get_env () {
		ConfigurationSet set = myConfigService.getEnv(APP, "1.1", "UAT");
		assertNotNull (set);
		assertEquals(APP, set.getId());
		assertEquals("myapp", set.getName());
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
		myConfigService.getEnv(APP, "1.x", "UAT");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void get_env_no_environment () {
		myConfigService.getEnv(APP, "1.1", "XXX");
	}
	
	@Test
	public void applications() {
		List<ApplicationSummary> applications = myConfigService.getApplications().getSummaries();
		assertNotNull (applications);
		{
			ApplicationSummary app = applications.get(0);
			assertEquals ("APP", app.getId());
			assertEquals ("myapp", app.getName());
			assertEquals (4, app.getVersionCount());
			assertEquals (3, app.getKeyCount());
			assertEquals (4, app.getEnvironmentCount());
			assertEquals (40, app.getConfigCount());
			assertEquals (28, app.getValueCount());
		}
		{
			ApplicationSummary app = applications.get(1);
			assertEquals ("APP2", app.getId());
			assertEquals ("anotherapp", app.getName());
			assertEquals (2, app.getVersionCount());
			assertEquals (2, app.getKeyCount());
			assertEquals (4, app.getEnvironmentCount());
			assertEquals (12, app.getConfigCount());
			assertEquals (0, app.getValueCount());
		}
	}
	
	@Test
	public void applicationCreate () {
		doApplicationCreate("APP_CREATE", "test");
	}
	
	@Test
	public void applicationCreate_special_characters () {
		doApplicationCreate("APP_SPECIAL", "te - _ s.t");
	}

	protected void doApplicationCreate(String id, String name) {
		ApplicationSummary summary = myConfigService.createApplication(id, name);
		assertNotNull (summary);
		assertEquals (name, summary.getName());
		assertEquals (id, summary.getId());
		assertEquals (0, summary.getVersionCount());
		assertEquals (0, summary.getKeyCount());
		assertEquals (0, summary.getEnvironmentCount());
		assertEquals (0, summary.getConfigCount());
		assertEquals (0, summary.getValueCount());
	}
	
	@Test
	public void applicationCreate_null () {
		try {
			myConfigService.createApplication("A", null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_null () {
		try {
			myConfigService.createApplication(null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_blank () {
		try {
			myConfigService.createApplication("A", "");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_blank () {
		try {
			myConfigService.createApplication("", null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: must be a sequence of ASCII letters, underscore(_) or digits",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_spaces () {
		try {
			myConfigService.createApplication("A", "     ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: may not be blank",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_spaces () {
		try {
			myConfigService.createApplication("   ", null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: must be a sequence of ASCII letters, underscore(_) or digits",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_unrecognized_characters () {
		try {
			myConfigService.createApplication("A", "<te/st\u00E9>");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: must be a sequence of ASCII letters, dash(-), underscore(_) and/or spaces ( )",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_unrecognized_characters () {
		try {
			myConfigService.createApplication("TEST APP", null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: must be a sequence of ASCII letters, underscore(_) or digits",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_trim () {
		try {
			myConfigService.createApplication("A", "  myapp   ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: may not have leading or trailing blanks",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_trim () {
		try {
			myConfigService.createApplication("  A  ", "  myapp   ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: must be a sequence of ASCII letters, underscore(_) or digits",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_too_long () {
		try {
			myConfigService.createApplication("A", StringUtils.repeat("x", 81));
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-001] Application name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void applicationCreate_id_too_long () {
		try {
			myConfigService.createApplication(StringUtils.repeat("A",17), null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-008] Application ID is invalid: size must be between 0 and 16",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test(expected = ApplicationIDAlreadyDefinedException.class)
	public void applicationCreate_id_not_unique () {
		myConfigService.createApplication("A", "test2");
		myConfigService.createApplication("A", "test3");
	}
	
	@Test(expected = ApplicationNameAlreadyDefinedException.class)
	public void applicationCreate_not_unique () {
		myConfigService.createApplication("A", "test2");
		myConfigService.createApplication("B", "test2");
	}

	@Test
	public void applicationDelete () {
		myConfigService.createApplication("app_delete", "test3").getId();
		Ack ack = myConfigService.deleteApplication("app_delete");
		assertNotNull (ack);
		assertTrue (ack.isSuccess());
	}

	@Test
	public void applicationDelete_cannot () {
		Ack ack = myConfigService.deleteApplication("app_xxx");
		assertNotNull (ack);
		assertFalse (ack.isSuccess());
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_configuration_noapp () {
		myConfigService.getApplicationConfiguration("app_xxx");
	}
		
	@Test
	public void application_configuration () {
		ApplicationConfiguration app = myConfigService.getApplicationConfiguration(APP);
		assertNotNull (app);
		assertEquals (APP, app.getId());
		assertEquals ("myapp", app.getName());
		// Versions
		List<VersionSummary> versions = app.getVersionSummaryList();
		assertNotNull (versions);
		assertEquals (4, versions.size());
		assertVersionSummary ("1.0", 2, 8, 8, versions.get(0));
		assertVersionSummary ("1.1", 2, 8, 8, versions.get(1));
		assertVersionSummary ("1.2", 3, 12, 12, versions.get(2));
		assertVersionSummary ("1.3", 3, 12, 0, versions.get(3));
		// Environments
		List<EnvironmentSummary> environments = app.getEnvironmentSummaryList();
		assertNotNull (environments);
		assertEquals (4, environments.size());
		assertEnvironmentSummary ("DEV", 10, 7, environments.get(0));
		assertEnvironmentSummary ("ACC", 10, 7, environments.get(1));
		assertEnvironmentSummary ("UAT", 10, 7, environments.get(2));
		assertEnvironmentSummary ("PROD", 10, 7, environments.get(3));
		// Keys
		List<KeySummary> keys = app.getKeySummaryList();
		assertNotNull (keys);
		assertEquals (3, keys.size());
		assertKeySummary ("jdbc.password", "Password used to connect to the database", 4, 16, 12, keys.get(0));
		assertKeySummary ("jdbc.url", "URL used to connect to the database", 2, 8, 4, keys.get(1));
		assertKeySummary ("jdbc.user", "User used to connect to the database", 4, 16, 12, keys.get(2));
	}
	
	@Test
	public void version_create () throws DataSetException, SQLException {
		// Checks the table
		assertRecordNotExists ("select * from version where application = 'APP' and name = '1.4'");
		Ack ack = myConfigService.createVersion(APP, "1.4");
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists ("select * from version where application = 'APP' and name = '1.4'");
	}

	@Test
	public void version_create_null () {
		try {
			myConfigService.createVersion(APP, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-002] Version name is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void version_create_spaces () {
		try {
			myConfigService.createVersion(APP, " ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-002] Version name is invalid: may not be blank",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}		

	@Test
	public void version_create_trim () {
		try {
			myConfigService.createVersion(APP, " myversion ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-002] Version name is invalid: may not have leading or trailing blanks",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void version_create_blank () {
		try {
			myConfigService.createVersion(APP, "");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-002] Version name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void version_create_too_long () {
		try {
			myConfigService.createVersion(APP, StringUtils.repeat("x", 81));
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-002] Version name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_create_noapp () {
		myConfigService.createVersion("app_xxx", "1.3");
	}
	
	@Test(expected = VersionAlreadyDefinedException.class)
	public void version_create_exists () {
		myConfigService.createVersion(APP, "1.1");
	}
	
	@Test
	public void version_delete () throws DataSetException, SQLException {
		myConfigService.createVersion(APP, "2.0");
		assertRecordExists("select * from version where application = 'APP' and name = '2.0'");
		Ack ack = myConfigService.deleteVersion(APP, "2.0");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from version where application = 'APP' and name = '2.0'");
	}
	
	@Test
	public void version_delete_none () throws DataSetException, SQLException {
		Ack ack = myConfigService.deleteVersion(APP, "2.1");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void version_delete_noapp () {
		myConfigService.deleteVersion("app_xxx", "1.0");
	}
	
	@Test
	public void environment_create () throws DataSetException, SQLException {
		Ack ack = myConfigService.createEnvironment(APP, "TEST");
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists ("select * from environment where application = 'APP' and name = 'TEST'");
	}

	@Test
	public void environment_create_null () {
		try {
			myConfigService.createEnvironment(APP, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-005] Environment name is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void environment_create_spaces () {
		try {
			myConfigService.createEnvironment(APP, "  ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-005] Environment name is invalid: may not be blank",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void environment_create_trim () {
		try {
			myConfigService.createEnvironment(APP, " myenv  ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-005] Environment name is invalid: may not have leading or trailing blanks",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void environment_create_blank () {
		try {
			myConfigService.createEnvironment(APP, "");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-005] Environment name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	

	@Test
	public void environment_create_too_long () {
		try {
			myConfigService.createEnvironment(APP, StringUtils.repeat("x", 81));
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-005] Environment name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}	
	
	@Test(expected = ApplicationNotFoundException.class)
	public void environment_create_noapp () {
		myConfigService.createEnvironment("app_xxx", "TEST");
	}
	
	@Test(expected = EnvironmentAlreadyDefinedException.class)
	public void environment_create_exists () {
		myConfigService.createEnvironment(APP, "DEV");
	}
	
	@Test
	public void environment_delete () throws DataSetException, SQLException {
		myConfigService.createEnvironment(APP, "TEST2");
		assertRecordExists("select * from environment where application = 'APP' and name = 'TEST2'");
		Ack ack = myConfigService.deleteEnvironment(APP, "TEST2");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from environment where application = 'APP' and name = 'TEST2'");
	}
	
	@Test
	public void environment_delete_none () {
		Ack ack = myConfigService.deleteEnvironment(APP, "TEST3");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void environment_delete_noapp () {
		myConfigService.deleteEnvironment("app_xxx", "TEST");
	}
	
	@Test
	public void key_create () throws DataSetException, SQLException {
		Ack ack = myConfigService.createKey(APP, "key1", "Description for key 1", null, null);
		assertTrue (ack.isSuccess());
		// Checks the table
		assertRecordExists("select * from appkey where application = 'APP' and name = 'key1' and description = 'Description for key 1'");
	}
	
	@Test
	public void key_create_null () {
		try {
			myConfigService.createKey(APP, null, null, null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-003] Key name is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void key_create_spaces () {
		try {
			myConfigService.createKey(APP, "  ", null, null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-003] Key name is invalid: may not be blank",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void key_create_trim () {
		try {
			myConfigService.createKey(APP, " mykey  ", null, null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-003] Key name is invalid: may not have leading or trailing blanks",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void key_create_blank () {
		try {
			myConfigService.createKey(APP, "", null, null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-003] Key name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void key_create_too_long () {
		try {
			myConfigService.createKey(APP, StringUtils.repeat("x", 81), null, null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-003] Key name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void key_create_description_too_long () {
		try {
			myConfigService.createKey(APP, StringUtils.repeat("x", 80), StringUtils.repeat("x", 501), null, null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-004] Key description is invalid: size must be between 0 and 500",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void key_create_noapp () {
		myConfigService.createKey("app_xxx", "key2", "Description for key 2", null, null);
	}
	
	@Test(expected = KeyAlreadyDefinedException.class)
	public void key_create_exists () {
		myConfigService.createKey(APP, "jdbc.user", "New description", null, null);
	}

	@Test(expected = ApplicationNotFoundException.class)
	public void key_update_noapp() {
		myConfigService.updateKey("app_xxx", "key1", "xxx");
	}

	@Test(expected = KeyNotFoundException.class)
	public void key_update_nokey() {
		myConfigService.updateKey("APP2", "keyx", "xxx");
	}

	@Test(expected = ValidationException.class)
	public void key_update_baddescription() {
		myConfigService.updateKey("APP2", "key1", "    ");
	}

	@Test
	public void key_update() throws DataSetException, SQLException {
		assertRecordExists("select * from appkey where application = 'APP2' and name = 'key1' and description = 'Key 1'");
		myConfigService.updateKey("APP2", "key1", "xxx");
		assertRecordExists("select * from appkey where application = 'APP2' and name = 'key1' and description = 'xxx'");
	}
	
	@Test
	public void key_delete () throws DataSetException, SQLException {
		myConfigService.createKey(APP, "key3", "Description for key 3", null, null);
		assertRecordExists("select * from appkey where application = 'APP' and name = 'key3' and description = 'Description for key 3'");
		Ack ack = myConfigService.deleteKey(APP, "key3");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from appkey where application = 'APP' and name = 'key3' and description = 'Description for key 3'");
	}
	
	@Test
	public void key_delete_none () {
		Ack ack = myConfigService.deleteKey(APP, "key4");
		assertFalse (ack.isSuccess());
	}
	
	@Test (expected= ApplicationNotFoundException.class)
	public void key_delete_noapp () {
		myConfigService.deleteKey("app_xxx", "key5");
	}
	
	@Test
	public void matrix () throws JsonGenerationException, JsonMappingException, IOException {
		MatrixConfiguration configuration = myConfigService.keyVersionConfiguration(APP);
		assertNotNull (configuration);
		assertJSONEquals (
				new MatrixConfiguration(APP, "myapp",
					Arrays.asList(
							new MatrixVersionConfiguration(
									"1.0",
									Arrays.asList("jdbc.password", "jdbc.user")),
							new MatrixVersionConfiguration(
									"1.1",
									Arrays.asList("jdbc.password", "jdbc.user")),
							new MatrixVersionConfiguration(
									"1.2",
									Arrays.asList("jdbc.password", "jdbc.url", "jdbc.user")),
							new MatrixVersionConfiguration(
									"1.3",
									Arrays.asList("jdbc.password", "jdbc.url", "jdbc.user"))),
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"),
							new Key("jdbc.user", "User used to connect to the database", "plain", null))
					),
				configuration);
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void matrix_no_app () {
		myConfigService.keyVersionConfiguration("app_xxx");
	}
	
	@Test
	public void environment_configuration() throws JsonGenerationException, JsonMappingException, IOException {
		EnvironmentConfiguration configuration = myConfigService.getEnvironmentConfiguration(APP, "ACC");
		assertNotNull (configuration);
		assertJSONEquals (
				new EnvironmentConfiguration(APP, "myapp", "ACC", "DEV", "UAT",
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<ConditionalValue>(
									"1.0",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.0 jdbc.password ACC"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.0 jdbc.user ACC"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.1",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.1 jdbc.password ACC"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.1 jdbc.user ACC"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.2",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.2 jdbc.password ACC"))
									.put("jdbc.url", new ConditionalValue(true, "1.2 jdbc.url ACC"))
									.put("jdbc.user", new ConditionalValue(true, "1.2 jdbc.user ACC"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.3",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, ""))
									.put("jdbc.url", new ConditionalValue(true, ""))
									.put("jdbc.user", new ConditionalValue(true, ""))
									.build())
							)
					),
				configuration);
	}
	
	/**
	 * Test for an application where environments, keys, versions and matrix have been configured, but where no value
	 * has been added yet.
	 */
	@Test
	public void environment_configuration_no_config() throws JsonGenerationException, JsonMappingException, IOException {
		EnvironmentConfiguration configuration = myConfigService.getEnvironmentConfiguration("APP2", "DEV");
		assertNotNull (configuration);
		assertJSONEquals (
				new EnvironmentConfiguration("APP2", "anotherapp", "DEV", null, "ACC",
					Arrays.asList(
							new Key("key1", "Key 1", "plain", null),
							new Key("key2", "Key 2", "plain", null)),
					Arrays.asList(
							new IndexedValues<ConditionalValue>(
									"1.0.0",
									MapBuilder.<String,ConditionalValue>create()
									.put("key1", new ConditionalValue(true, ""))
									.put("key2", new ConditionalValue(false, ""))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.0.1",
									MapBuilder.<String,ConditionalValue>create()
									.put("key1", new ConditionalValue(true, ""))
									.put("key2", new ConditionalValue(true, ""))
									.build())
							)
					),
				configuration);
	}

	@Test
	public void environment_configuration_no_next_version() throws JsonGenerationException, JsonMappingException, IOException {
		EnvironmentConfiguration configuration = myConfigService.getEnvironmentConfiguration(APP, "PROD");
		assertNotNull (configuration);
		assertJSONEquals (
				new EnvironmentConfiguration(APP, "myapp", "PROD", "UAT", null,
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<ConditionalValue>(
									"1.0",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.0 jdbc.password PROD"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.0 jdbc.user PROD"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.1",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.1 jdbc.password PROD"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.1 jdbc.user PROD"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.2",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.2 jdbc.password PROD"))
									.put("jdbc.url", new ConditionalValue(true, "1.2 jdbc.url PROD"))
									.put("jdbc.user", new ConditionalValue(true, "1.2 jdbc.user PROD"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.3",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, ""))
									.put("jdbc.url", new ConditionalValue(true, ""))
									.put("jdbc.user", new ConditionalValue(true, ""))
									.build())
					)
				),
				configuration);
	}
	
	@Test
	public void environment_configuration_no_previous_version() throws JsonGenerationException, JsonMappingException, IOException {
		EnvironmentConfiguration configuration = myConfigService.getEnvironmentConfiguration(APP, "DEV");
		assertNotNull (configuration);
		assertJSONEquals (
				new EnvironmentConfiguration(APP, "myapp", "DEV", null, "ACC",
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<ConditionalValue>(
									"1.0",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.0 jdbc.password DEV"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.0 jdbc.user DEV"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.1",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.1 jdbc.password DEV"))
									.put("jdbc.url", new ConditionalValue(false, ""))
									.put("jdbc.user", new ConditionalValue(true, "1.1 jdbc.user DEV"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.2",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, "1.2 jdbc.password DEV"))
									.put("jdbc.url", new ConditionalValue(true, "1.2 jdbc.url DEV"))
									.put("jdbc.user", new ConditionalValue(true, "1.2 jdbc.user DEV"))
									.build()),
							new IndexedValues<ConditionalValue>(
									"1.3",
									MapBuilder.<String,ConditionalValue>create()
									.put("jdbc.password", new ConditionalValue(true, ""))
									.put("jdbc.url", new ConditionalValue(true, ""))
									.put("jdbc.user", new ConditionalValue(true, ""))
									.build())
					)
				),
				configuration);
	}

	@Test(expected = ApplicationNotFoundException.class)
	public void environment_configuration_no_app() {
		myConfigService.getEnvironmentConfiguration("app_xxx", "");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void environment_configuration_no_version() {
		myConfigService.getEnvironmentConfiguration(APP, "xxx");
	}
	
	@Test
	public void key_configuration() throws JsonGenerationException, JsonMappingException, IOException {
		KeyConfiguration configuration = myConfigService.getKeyConfiguration(APP, "jdbc.url");
		assertNotNull (configuration);
		assertJSONEquals (
				new KeyConfiguration(APP, "myapp", new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"), "jdbc.password", "jdbc.user",
					Arrays.asList(
							new Version("1.2"),
							new Version("1.3")),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									MapBuilder.<String,String>create()
										.put("1.2", "1.2 jdbc.url DEV")
										.build()),
							new IndexedValues<String>(
									"ACC",
									MapBuilder.<String,String>create()
										.put("1.2", "1.2 jdbc.url ACC")
										.build()),
							new IndexedValues<String>(
									"UAT",
									MapBuilder.<String,String>create()
										.put("1.2", "1.2 jdbc.url UAT")
										.build()),
							new IndexedValues<String>(
									"PROD",
									MapBuilder.<String,String>create()
										.put("1.2", "1.2 jdbc.url PROD")
										.build())
							)
					),
				configuration);
	}
	
	@Test
	public void key_configuration_no_previous() throws JsonGenerationException, JsonMappingException, IOException {
		KeyConfiguration configuration = myConfigService.getKeyConfiguration(APP, "jdbc.password");
		assertNotNull (configuration);
		assertJSONEquals (
				new KeyConfiguration(APP, "myapp", new Key("jdbc.password", "Password used to connect to the database", "plain", null), null, "jdbc.url",
					Arrays.asList(
							new Version("1.0"),
							new Version("1.1"),
							new Version("1.2"),
							new Version("1.3")),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.password DEV")
										.put("1.1", "1.1 jdbc.password DEV")
										.put("1.2", "1.2 jdbc.password DEV")
										.build()),
							new IndexedValues<String>(
									"ACC",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.password ACC")
										.put("1.1", "1.1 jdbc.password ACC")
										.put("1.2", "1.2 jdbc.password ACC")
										.build()),
							new IndexedValues<String>(
									"UAT",
										MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.password UAT")
										.put("1.1", "1.1 jdbc.password UAT")
										.put("1.2", "1.2 jdbc.password UAT")
										.build()),
							new IndexedValues<String>(
									"PROD",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.password PROD")
										.put("1.1", "1.1 jdbc.password PROD")
										.put("1.2", "1.2 jdbc.password PROD")
										.build())
							)
					),
				configuration);
	}
	
	@Test
	public void key_configuration_no_next() throws JsonGenerationException, JsonMappingException, IOException {
		KeyConfiguration configuration = myConfigService.getKeyConfiguration(APP, "jdbc.user");
		assertNotNull (configuration);
		assertJSONEquals (
				new KeyConfiguration(APP, "myapp", new Key("jdbc.user", "User used to connect to the database", "plain", null), "jdbc.url", null,
					Arrays.asList(
							new Version("1.0"),
							new Version("1.1"),
							new Version("1.2"),
							new Version("1.3")),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.user DEV")
										.put("1.1", "1.1 jdbc.user DEV")
										.put("1.2", "1.2 jdbc.user DEV")
										.build()),
							new IndexedValues<String>(
									"ACC",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.user ACC")
										.put("1.1", "1.1 jdbc.user ACC")
										.put("1.2", "1.2 jdbc.user ACC")
										.build()),
							new IndexedValues<String>(
									"UAT",
										MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.user UAT")
										.put("1.1", "1.1 jdbc.user UAT")
										.put("1.2", "1.2 jdbc.user UAT")
										.build()),
							new IndexedValues<String>(
									"PROD",
									MapBuilder.<String,String>create()
										.put("1.0", "1.0 jdbc.user PROD")
										.put("1.1", "1.1 jdbc.user PROD")
										.put("1.2", "1.2 jdbc.user PROD")
										.build())
							)
					),
				configuration);
	}

	@Test(expected = ApplicationNotFoundException.class)
	public void key_configuration_no_app() {
		myConfigService.getKeyConfiguration("app_xxx", "");
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void key_configuration_no_version() {
		myConfigService.getKeyConfiguration(APP, "xxx");
	}
	
	@Test
	public void version_configuration() throws JsonGenerationException, JsonMappingException, IOException {
		VersionConfiguration configuration = myConfigService.getVersionConfiguration(APP, "1.1");
		assertNotNull (configuration);
		assertJSONEquals (
				new VersionConfiguration(APP, "myapp", "1.1", "1.0", "1.2",
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									map (
											"jdbc.password", "1.1 jdbc.password DEV",
											"jdbc.user", "1.1 jdbc.user DEV")),
							new IndexedValues<String>(
									"ACC",
									map (
											"jdbc.password", "1.1 jdbc.password ACC",
											"jdbc.user", "1.1 jdbc.user ACC")),
							new IndexedValues<String>(
									"UAT",
									map (
											"jdbc.password", "1.1 jdbc.password UAT",
											"jdbc.user", "1.1 jdbc.user UAT")),
							new IndexedValues<String>(
									"PROD",
									map (
											"jdbc.password", "1.1 jdbc.password PROD",
											"jdbc.user", "1.1 jdbc.user PROD"))
							)
					),
				configuration);
	}
	
	/**
	 * Test for an application where environments, keys, versions and matrix have been configured, but where no value
	 * has been added yet.
	 */
	@Test
	public void version_configuration_no_config() throws JsonGenerationException, JsonMappingException, IOException {
		VersionConfiguration configuration = myConfigService.getVersionConfiguration("APP2", "1.0.1");
		assertNotNull (configuration);
		assertJSONEquals (
				new VersionConfiguration("APP2", "anotherapp", "1.0.1", "1.0.0", null,
					Arrays.asList(
							new Key("key1", "Key 1", "plain", null),
							new Key("key2", "Key 2", "plain", null)),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									Collections.<String,String>emptyMap()),
							new IndexedValues<String>(
									"ACC",
									Collections.<String,String>emptyMap()),
							new IndexedValues<String>(
									"UAT",
									Collections.<String,String>emptyMap()),
							new IndexedValues<String>(
									"PROD",
									Collections.<String,String>emptyMap())
							)
					),
				configuration);
	}

	@Test
	public void version_configuration_no_next_version() throws JsonGenerationException, JsonMappingException, IOException {
		VersionConfiguration configuration = myConfigService.getVersionConfiguration(APP, "1.3");
		assertNotNull (configuration);
		assertJSONEquals (
				new VersionConfiguration(APP, "myapp", "1.3", "1.2", null,
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.url", "URL used to connect to the database", "regex", "jdbc:.*"),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									map ()),
							new IndexedValues<String>(
									"ACC",
									map ()),
							new IndexedValues<String>(
									"UAT",
									map ()),
							new IndexedValues<String>(
									"PROD",
									map ())
							)
					),
				configuration);
	}
	
	@Test
	public void version_configuration_no_previous_version() throws JsonGenerationException, JsonMappingException, IOException {
		VersionConfiguration configuration = myConfigService.getVersionConfiguration(APP, "1.0");
		assertNotNull (configuration);
		assertJSONEquals (
				new VersionConfiguration(APP, "myapp", "1.0", null, "1.1",
					Arrays.asList(
							new Key("jdbc.password", "Password used to connect to the database", "plain", null),
							new Key("jdbc.user", "User used to connect to the database", "plain", null)),
					Arrays.asList(
							new IndexedValues<String>(
									"DEV",
									map (
											"jdbc.password", "1.0 jdbc.password DEV",
											"jdbc.user", "1.0 jdbc.user DEV")),
							new IndexedValues<String>(
									"ACC",
									map (
											"jdbc.password", "1.0 jdbc.password ACC",
											"jdbc.user", "1.0 jdbc.user ACC")),
							new IndexedValues<String>(
									"UAT",
									map (
											"jdbc.password", "1.0 jdbc.password UAT",
											"jdbc.user", "1.0 jdbc.user UAT")),
							new IndexedValues<String>(
									"PROD",
									map (
											"jdbc.password", "1.0 jdbc.password PROD",
											"jdbc.user", "1.0 jdbc.user PROD"))
							)
					),
				configuration);
	}

	@Test(expected = ApplicationNotFoundException.class)
	public void version_configuration_no_app() {
		myConfigService.getVersionConfiguration("app_xxx", "");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void version_configuration_no_version() {
		myConfigService.getVersionConfiguration(APP, "1.x");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_configuration_update_no_app () {
		myConfigService.updateConfiguration("app_xxx", null);
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void configuration_update_no_version () {
		myConfigService.updateConfiguration(APP, new ConfigurationUpdates(Arrays.asList(
				new ConfigurationUpdate("UAT", "1.x", "jdbc.user", "xxx")
				)));
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void configuration_update_no_environment () {
		myConfigService.updateConfiguration(APP, new ConfigurationUpdates(Arrays.asList(
				new ConfigurationUpdate("XXX", "1.0", "jdbc.user", "xxx")
				)));
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void configuration_update_no_key () {
		myConfigService.updateConfiguration(APP, new ConfigurationUpdates(Arrays.asList(
				new ConfigurationUpdate("UAT", "1.0", "jdbc.xxx", "xxx")
				)));
	}
	
	@Test(expected = MatrixNotFoundException.class)
	public void configuration_update_no_matrix () {
		myConfigService.updateConfiguration(APP, new ConfigurationUpdates(Arrays.asList(
				new ConfigurationUpdate("UAT", "1.0", "jdbc.url", "xxx")
				)));
	}
	
	@Test
	public void configuration_update_none () throws DataSetException, SQLException {
		assertRecordCount (8, "select * from config where application = 'APP' and version = '1.0'");
		Ack ack = myConfigService.updateConfiguration(APP, new ConfigurationUpdates(
			Arrays.<ConfigurationUpdate>asList()
		));
		assertTrue (ack.isSuccess());
		assertRecordCount (8, "select * from config where application = 'APP' and version = '1.0'");
	}
	
	@Test
	public void configuration_update_several () throws DataSetException, SQLException {
		assertRecordCount (8, "select * from config where application = 'APP' and version = '1.1'");
		assertRecordValue ("1.1 jdbc.password UAT", "value", "select value from config where application = 'APP' and version = '1.1' and environment = 'UAT' and appkey = 'jdbc.password'");
		assertRecordValue ("1.1 jdbc.user UAT", "value", "select value from config where application = 'APP' and version = '1.1' and environment = 'UAT' and appkey = 'jdbc.user'");
		Ack ack = myConfigService.updateConfiguration(APP, new ConfigurationUpdates(
			Arrays.asList(
				new ConfigurationUpdate ("UAT", "1.1", "jdbc.user", "x1"),
				new ConfigurationUpdate ("UAT", "1.1", "jdbc.password", "x2")
			)
		));
		assertTrue (ack.isSuccess());
		assertRecordCount (8, "select * from config where application = 'APP' and version = '1.1'");
		assertRecordValue ("x2", "value", "select value from config where application = 'APP' and version = '1.1' and environment = 'UAT' and appkey = 'jdbc.password'");
		assertRecordValue ("x1", "value", "select value from config where application = 'APP' and version = '1.1' and environment = 'UAT' and appkey = 'jdbc.user'");
	}
	
	@Test
	public void version_key_add () throws DataSetException, SQLException {
		assertRecordNotExists("select * from version_key where application = 'APP' and version = '1.1' and appkey = 'jdbc.url'");
		Ack ack = myConfigService.addKeyVersion(APP, "1.1", "jdbc.url");
		assertTrue (ack.isSuccess());
		assertRecordExists("select * from version_key where application = 'APP' and version = '1.1' and appkey = 'jdbc.url'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_key_add_noapp () throws DataSetException, SQLException {
		myConfigService.addKeyVersion("app_xxx", "1.1", "jdbc.url");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void version_key_add_noversion () throws DataSetException, SQLException {
		myConfigService.addKeyVersion(APP, "1.X", "jdbc.url");
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void version_key_add_nokey () throws DataSetException, SQLException {
		myConfigService.addKeyVersion(APP, "1.0", "jdbc.xxx");
	}
	
	@Test(expected = KeyAlreadyInVersionException.class)
	public void version_key_add_duplicate () throws DataSetException, SQLException {
		assertRecordExists("select * from version_key where application = 'APP' and version = '1.2' and appkey = 'jdbc.url'");
		myConfigService.addKeyVersion(APP, "1.2", "jdbc.url");
	}
	
	@Test
	public void version_key_remove () throws DataSetException, SQLException {
		assertRecordExists("select * from version_key where application = 'APP' and version = '1.0' and appkey = 'jdbc.password'");
		Ack ack = myConfigService.removeKeyVersion(APP, "1.0", "jdbc.password");
		assertTrue (ack.isSuccess());
		assertRecordNotExists("select * from version_key where application = 'APP' and version = '1.0' and appkey = 'jdbc.password'");
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void version_key_remove_noapp () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion("app_xxx", "1.1", "jdbc.url");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void version_key_remove_noversion () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion(APP, "1.X", "jdbc.url");
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void version_key_remove_nokey () throws DataSetException, SQLException {
		myConfigService.removeKeyVersion(APP, "1.0", "jdbc.xxx");
	}
	
	@Test
	public void version_key_remove_none () throws DataSetException, SQLException {
		assertRecordNotExists("select * from version_key where application = 'APP' and version = '1.1' and appkey = 'jdbc.url'");
		Ack ack = myConfigService.removeKeyVersion(APP, "1.1", "jdbc.url");
		assertFalse (ack.isSuccess());
	}
	
	private Map<String, String> map(String... args) {
		Map<String, String> map = new TreeMap<String, String>();
		String key = null;
		String value = null;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (i % 2 == 0) {
				key = arg;
			} else {
				value = arg;
				map.put(key, value);
			}
		}
		return map;
	}
	
	private <T> void assertJSONEquals (T a, T b) throws JsonGenerationException, JsonMappingException, IOException {
		String ja = toJSON (a);
		String jb = toJSON (b);
		assertEquals (ja, jb);
	}
	
	private String toJSON (Object o) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
		ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
		return writer.writeValueAsString(o);
	}

	private void assertKeySummary(String name, String description, int versionCount, int configCount, int valueCount, KeySummary keySummary) {
		assertNotNull (keySummary);
		assertEquals (name, keySummary.getName());
		assertEquals (description, keySummary.getDescription());
		assertEquals (versionCount, keySummary.getVersionCount());
		assertEquals (configCount, keySummary.getConfigCount());
		assertEquals (valueCount, keySummary.getValueCount());
	}

	private void assertEnvironmentSummary(String name, int configCount, int valueCount,
			EnvironmentSummary environmentSummary) {
		assertNotNull(environmentSummary);
		assertEquals (name, environmentSummary.getName());		
		assertEquals (configCount, environmentSummary.getConfigCount());
		assertEquals (valueCount, environmentSummary.getValueCount());		
	}

	private void assertVersionSummary(String name, int keyCount, int configCount, int valueCount,
			VersionSummary versionSummary) {
		assertNotNull (versionSummary);
		assertEquals (name, versionSummary.getName());
		assertEquals (keyCount, versionSummary.getKeyCount());
		assertEquals (configCount, versionSummary.getConfigCount());
		assertEquals (valueCount, versionSummary.getValueCount());
	}

}
