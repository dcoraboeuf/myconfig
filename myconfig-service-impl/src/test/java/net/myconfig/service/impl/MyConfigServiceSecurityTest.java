package net.myconfig.service.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
		ApplicationSummary app = myconfig.createApplication("xxx1");
		assertNotNull(app);
		int id = app.getId();
		// Checks the grants
		assertRecordNotExists("select * from appgrants where user = 'admin' and application = %d", id);
	}

	@Test
	public void createApplication_user_granted() throws DataSetException, SQLException {
		asUser("userx").grant(UserFunction.app_create);
		ApplicationSummary app = myconfig.createApplication("xxx2");
		assertNotNull(app);
		int id = app.getId();
		// Checks the grants
		assertRecordCount(AppFunction.values().length, "select * from appgrants where user = 'userx' and application = %d", id);
		for (AppFunction fn : AppFunction.values()) {
			assertRecordExists("select * from appgrants where user = 'userx' and application = %d and grantedfunction = '%s'", id, fn.name());
		}
	}

	@Test(expected = AccessDeniedException.class)
	public void createApplication_user_not_granted() throws SQLException {
		asUser();
		myconfig.createApplication("xxx");
	}

	@Test
	public void deleteApplication_admin() throws SQLException {
		asAdmin();
		Ack ack = myconfig.deleteApplication(10);
		assertFalse(ack.isSuccess());
	}

	@Test
	public void deleteApplication_user_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_delete);
		Ack ack = myconfig.deleteApplication(1);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void deleteApplication_user_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_delete);
		myconfig.deleteApplication(10);
	}

	@Test
	public void getApplicationConfiguration_admin() throws SQLException {
		asAdmin();
		ApplicationConfiguration conf = myconfig.getApplicationConfiguration(1);
		assertNotNull(conf);
	}

	@Test
	public void getApplicationConfiguration_user_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view);
		ApplicationConfiguration conf = myconfig.getApplicationConfiguration(1);
		assertNotNull(conf);
	}

	@Test(expected = AccessDeniedException.class)
	public void getApplicationConfiguration_user_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view);
		myconfig.getApplicationConfiguration(10);
	}

	@Test
	public void updateConfiguration_admin() throws SQLException {
		asAdmin();
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(1, updates);
	}

	@Test
	public void updateConfiguration_user_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_config).grant(1, "UAT", EnvFunction.env_config);
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(1, updates);
	}

	@Test(expected = AccessDeniedException.class)
	public void updateConfiguration_user_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_config);
		ConfigurationUpdates updates = new ConfigurationUpdates(asList(new ConfigurationUpdate("DEV", "1.0", "jdbc.password", "devpwd"), new ConfigurationUpdate("UAT", "1.0", "jdbc.password",
				"uatpwd")));
		myconfig.updateConfiguration(1, updates);
	}

	@Test
	public void getEnvironmentConfiguration_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "UAT", EnvFunction.env_view);
		EnvironmentConfiguration c = myconfig.getEnvironmentConfiguration(1, "UAT");
		assertNotNull(c);
	}

	@Test(expected = AccessDeniedException.class)
	public void getEnvironmentConfiguration_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_config);
		myconfig.getEnvironmentConfiguration(1, "UAT");
	}

	@Test(expected = AccessDeniedException.class)
	public void getEnv_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_config);
		myconfig.getEnv("myapp", "1.0", "UAT");
	}

	@Test
	public void getKeyConfiguration_all() throws SQLException {
		asAdmin();
		KeyConfiguration c = myconfig.getKeyConfiguration(1, "jdbc.user");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerVersionList = c.getEnvironmentValuesPerVersionList();
		List<String> envs = Lists.transform(environmentValuesPerVersionList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV", "UAT"), envs);
	}

	@Test
	public void getKeyConfiguration_restricted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_view);
		KeyConfiguration c = myconfig.getKeyConfiguration(1, "jdbc.user");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerVersionList = c.getEnvironmentValuesPerVersionList();
		List<String> envs = Lists.transform(environmentValuesPerVersionList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV"), envs);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getKey_not_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_view);
		myconfig.getKey("myapp", "1.0", "UAT", "jdbc.user");
	}
	
	@Test
	public void getKey_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_view);
		String value = myconfig.getKey("myapp", "1.0", "DEV", "jdbc.user");
		assertEquals ("1.0 jdbc.user DEV", value);
	}

	@Test
	public void getVersionConfiguration_all() throws SQLException {
		asAdmin();
		VersionConfiguration c = myconfig.getVersionConfiguration(1, "1.0");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerKeyList = c.getEnvironmentValuesPerKeyList();
		List<String> envs = Lists.transform(environmentValuesPerKeyList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV", "UAT"), envs);
	}

	@Test
	public void getVersionConfiguration_restricted() throws SQLException {
		asUser().grant(1, AppFunction.app_view).grant(1, "DEV", EnvFunction.env_view);
		VersionConfiguration c = myconfig.getVersionConfiguration(1, "1.0");
		assertNotNull(c);
		List<IndexedValues<String>> environmentValuesPerKeyList = c.getEnvironmentValuesPerKeyList();
		List<String> envs = Lists.transform(environmentValuesPerKeyList, IndexedValues.<String> indexFn());
		assertEquals(Arrays.asList("DEV"), envs);
	}
	
	@Test
	public void createEnvironment_admin() throws SQLException {
		asAdmin();
		Ack ack = myconfig.createEnvironment(1, "ADMIN");
		assertTrue (ack.isSuccess());
	}
	
	@Test
	public void createEnvironment_granted() throws SQLException {
		asUser().grant(1, AppFunction.app_envcreate);
		Ack ack = myconfig.createEnvironment(1, "ENVCREATE");
		assertTrue (ack.isSuccess());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void createEnvironment_not_granted() throws SQLException {
		asUser();
		myconfig.createEnvironment(1, "NO_ENVCREATE");
	}
	
	@Test
	public void deleteEnvironment_admin() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(1, "DEL_ENV_ADMIN");
		Ack ack = myconfig.deleteEnvironment(1, "DEL_ENV_ADMIN");
		assertTrue (ack.isSuccess());
	}
	
	@Test
	public void deleteEnvironment_granted() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(1, "DEL_ENV_GRANTED");
		asUser().grant(1, "DEL_ENV_GRANTED", EnvFunction.env_delete);
		Ack ack = myconfig.deleteEnvironment(1, "DEL_ENV_GRANTED");
		assertTrue (ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void deleteEnvironment_not_granted() throws SQLException {
		asAdmin();
		myconfig.createEnvironment(1, "DEL_ENV_NOT_GRANTED");
		asUser();
		myconfig.deleteEnvironment(1, "DEL_ENV_NOT_GRANTED");
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
