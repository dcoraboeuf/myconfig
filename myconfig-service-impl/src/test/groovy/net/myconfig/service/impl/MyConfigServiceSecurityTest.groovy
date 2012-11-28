package net.myconfig.service.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;
import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.core.model.IndexedValues;
import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.service.api.MyConfigService;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@TransactionConfiguration
@Transactional
public class MyConfigServiceSecurityTest extends AbstractSecurityTest {

	private static final String APP = "APP";
	@Autowired
	private MyConfigService myconfig;

	@Test
	public void getVersion_no_control() {
		String version = myconfig.getVersion();
		assertTrue(StringUtils.isNotBlank(version));
	}

	@Test
	public void getApplications_admin() throws SQLException {
		asAdmin();
		List<ApplicationSummary> apps = myconfig.getApplications().getSummaries();
		assertNotNull(apps);
	}

	@Test
	public void getApplications_user_granted() throws SQLException {
		asUser().grant(UserFunction.app_list);
		List<ApplicationSummary> apps = myconfig.getApplications().getSummaries();
		assertNotNull(apps);
	}

	@Test
	public void getApplications_user_not_granted() throws SQLException {
		asUser();
		List<ApplicationSummary> apps = myconfig.getApplications().getSummaries();
		assertNotNull(apps);
	}

	@Test
	public void createApplication_admin() throws DataSetException, SQLException {
		asAdmin();
		String id = "APP_ADMIN";
		ApplicationSummary app = myconfig.createApplication(id, "xxx1");
		assertNotNull(app);
		// Checks the grants
		assertRecordNotExists("select * from appgrants where user = 'admin' and application = '%s'", id);
	}

	@Test
	public void createApplication_user_granted() throws DataSetException, SQLException {
		asUser("userx").grant(UserFunction.app_create);
		String id = "APP_USER_GRANTED";
		ApplicationSummary app = myconfig.createApplication(id, "xxx2");
		assertNotNull(app);
		// Checks the grants
		assertRecordCount(AppFunction.values().length, "select * from appgrants where user = 'userx' and application = '%s'", id);
		for (AppFunction fn : AppFunction.values()) {
			assertRecordExists("select * from appgrants where user = 'userx' and application = '%s' and grantedfunction = '%s'", id, fn.name());
		}
	}

	@Test(expected = AccessDeniedException.class)
	public void createApplication_user_not_granted() throws SQLException {
		asUser();
		myconfig.createApplication("APP_USER_NOT_GRANTED", "xxx");
	}

	@Test
	public void deleteApplication_admin() throws SQLException {
		asAdmin();
		Ack ack = myconfig.deleteApplication("X");
		assertFalse(ack.isSuccess());
	}

	@Test
	public void deleteApplication_user_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_delete);
		Ack ack = myconfig.deleteApplication(APP);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void deleteApplication_user_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_delete);
		myconfig.deleteApplication("X");
	}
	
	@Test
	void getConfigurationDescription_admin() {
		asAdmin()
		def conf = myconfig.getConfigurationDescription(APP, "1.0")
		assert ["DEV", "ACC", "UAT", "PROD"] == conf.getEnvironments()*.getName()
	}
	
	@Test(expected = AccessDeniedException.class)
	void getConfigurationDescription_noview() {
		asUser()
		myconfig.getConfigurationDescription(APP, "1.0")
	}
	
	@Test
	void getConfigurationDescription_noenv() {
		asUser().grant(APP, AppFunction.app_view)
		def conf = myconfig.getConfigurationDescription(APP, "1.0")
		assert [] == conf.getEnvironments()*.getName()
	}
	
	@Test
	void getConfigurationDescription_someenv() {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view).grant(APP, "ACC", EnvFunction.env_view)
		def conf = myconfig.getConfigurationDescription(APP, "1.0")
		assert ["DEV", "ACC"] == conf.getEnvironments()*.getName()
	}

	@Test
	public void getApplicationConfiguration_admin() throws SQLException {
		asAdmin();
		ApplicationConfiguration conf = myconfig.getApplicationConfiguration(APP);
		assertNotNull(conf);
	}

	@Test
	public void getApplicationConfiguration_user_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view);
		ApplicationConfiguration conf = myconfig.getApplicationConfiguration(APP);
		assertNotNull(conf);
	}

	@Test(expected = AccessDeniedException.class)
	public void getApplicationConfiguration_user_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view);
		myconfig.getApplicationConfiguration("X");
	}

	@Test
	public void updateConfiguration_admin() throws SQLException {
		asAdmin();
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(APP, updates);
	}

	@Test
	public void updateConfiguration_user_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config).grant(APP, "UAT", EnvFunction.env_config);
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(APP, updates);
	}

	@Test(expected = AccessDeniedException.class)
	public void updateConfiguration_user_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config);
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(APP, updates);
	}

	@Test
	public void getEnvironmentConfiguration_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "UAT", EnvFunction.env_config);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(APP, "UAT");
		assertNotNull(c);
		assertNull(c.getPreviousEnvironment());
		assertNull(c.getNextEnvironment());
	}

	@Test
	public void getEnvironmentConfiguration_granted_next() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "PROD", EnvFunction.env_config).grant(APP, "UAT", EnvFunction.env_config);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(APP, "UAT");
		assertNotNull(c);
		assertNull(c.getPreviousEnvironment());
		assertEquals("PROD", c.getNextEnvironment());
	}

	@Test
	public void getEnvironmentConfiguration_granted_previous() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "ACC", EnvFunction.env_config).grant(APP, "UAT", EnvFunction.env_config);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(APP, "UAT");
		assertNotNull(c);
		assertEquals("ACC", c.getPreviousEnvironment());
		assertNull(c.getNextEnvironment());
	}

	@Test
	public void getEnvironmentConfiguration_granted_next_and_previous() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config).grant(APP, "ACC", EnvFunction.env_config).grant(APP, "UAT", EnvFunction.env_config);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(APP, "ACC");
		assertNotNull(c);
		assertEquals("DEV", c.getPreviousEnvironment());
		assertEquals("UAT", c.getNextEnvironment());
	}

	@Test
	public void getEnvironmentConfiguration_granted_next_and_previous_jump() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config).grant(APP, "ACC", EnvFunction.env_config).grant(APP, "PROD", EnvFunction.env_config);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(APP, "ACC");
		assertNotNull(c);
		assertEquals("DEV", c.getPreviousEnvironment());
		assertEquals("PROD", c.getNextEnvironment());
	}

	@Test(expected = AccessDeniedException.class)
	public void getEnvironmentConfiguration_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config);
		myconfig.getEnvironmentConfiguration(APP, "UAT");
	}

	@Test
	public void getEnv_admin() throws SQLException {
		asAdmin();
		ConfigurationSet env = myconfig.getEnv(APP, "1.0", "DEV");
		assertNotNull(env);
	}

	@Test
	public void getEnv_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view);
		ConfigurationSet env = myconfig.getEnv(APP, "1.0", "DEV");
		assertNotNull(env);
	}

	@Test
	public void getEnv_granted_through_config() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config);
		ConfigurationSet env = myconfig.getEnv(APP, "1.0", "DEV");
		assertNotNull(env);
	}

	@Test(expected = AccessDeniedException.class)
	public void getEnv_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_config);
		myconfig.getEnv(APP, "1.0", "UAT");
	}

	@Test
	public void getKeyConfiguration_all() throws SQLException {
		asAdmin();
		KeyConfiguration c = myconfig.getKeyConfiguration(APP, "jdbc.user");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerVersionList = c.getEnvironmentValuesPerVersionList();
		List<String> envs = Lists.transform(environmentValuesPerVersionList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV", "ACC", "UAT", "PROD"), envs);
	}

	@Test
	public void getKeyConfiguration_restricted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view);
		KeyConfiguration c = myconfig.getKeyConfiguration(APP, "jdbc.user");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerVersionList = c.getEnvironmentValuesPerVersionList();
		List<String> envs = Lists.transform(environmentValuesPerVersionList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV"), envs);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getKey_not_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view);
		myconfig.getKey(APP, "1.0", "UAT", "jdbc.user");
	}
	
	@Test
	public void getKey_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view);
		String value = myconfig.getKey(APP, "1.0", "DEV", "jdbc.user");
		assertEquals ("1.0 jdbc.user DEV", value);
	}

	@Test
	public void getVersionConfiguration_all() throws SQLException {
		asAdmin();
		VersionConfiguration c = myconfig.getVersionConfiguration(APP, "1.0");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerKeyList = c.getEnvironmentValuesPerKeyList();
		List<String> envs = Lists.transform(environmentValuesPerKeyList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV", "ACC", "UAT", "PROD"), envs);
	}

	@Test
	public void getVersionConfiguration_restricted() throws SQLException {
		asUser().grant(APP, AppFunction.app_view).grant(APP, "DEV", EnvFunction.env_view);
		VersionConfiguration c = myconfig.getVersionConfiguration(APP, "1.0");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerKeyList = c.getEnvironmentValuesPerKeyList();
		List<String> envs = Lists.transform(environmentValuesPerKeyList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV"), envs);
	}
	
	@Test
	public void createEnvironment_admin() throws SQLException {
		asAdmin();
		Ack ack = myconfig.createEnvironment(APP, "ADMIN");
		assertTrue (ack.isSuccess());
	}
	
	@Test
	public void createEnvironment_granted() throws SQLException {
		asUser().grant(APP, AppFunction.app_envcreate);
		Ack ack = myconfig.createEnvironment(APP, "ENVCREATE");
		assertTrue (ack.isSuccess());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void createEnvironment_not_granted() throws SQLException {
		asUser();
		myconfig.createEnvironment(APP, "NO_ENVCREATE");
	}
	
	@Test
	public void deleteEnvironment_admin() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(APP, "DEL_ENV_ADMIN");
		Ack ack = myconfig.deleteEnvironment(APP, "DEL_ENV_ADMIN");
		assertTrue (ack.isSuccess());
	}
	
	@Test
	public void deleteEnvironment_granted() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(APP, "DEL_ENV_GRANTED");
		asUser().grant(APP, "DEL_ENV_GRANTED", EnvFunction.env_delete);
		Ack ack = myconfig.deleteEnvironment(APP, "DEL_ENV_GRANTED");
		assertTrue (ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void deleteEnvironment_not_granted() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(APP, "DEL_ENV_NOT_GRANTED");
		asUser();
		myconfig.deleteEnvironment(APP, "DEL_ENV_NOT_GRANTED");
	}

	// TODO Ack createVersion(int id, String name);
	//
	// TODO Ack deleteVersion(int id, String name);
	//
	// TODO Ack deleteEnvironment(int id, String name);
	//
	// TODO Ack deleteKey(int id, String name);
	//
	// TODO Ack createKey(int id, String name, String description);
	//
	// TODO MatrixConfiguration keyVersionConfiguration(int id);
	//
	// TODO Ack addKeyVersion(int application, String version, String key);
	//
	// TODO Ack removeKeyVersion(int application, String version, String key);

	// TODO Ack updateKey(int application, String name, String description);

	// TODO String getKey(String application, String version, String
	// environment,
	// String key);
	//
	// TODO ConfigurationSet getEnv(String application, String version, String
	// environment);
}
