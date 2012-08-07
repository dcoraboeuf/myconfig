package net.myconfig.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationSummary;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

public class MyConfigServiceSecurityTest extends AbstractSecurityTest {

	@Autowired
	private MyConfigService myconfig;

	@Test
	public void getVersion_no_control() {
		String version = myconfig.getVersion();
		assertTrue(StringUtils.isNotBlank(version));
	}

	@Test
	public void getApplications_admin() {
		asAdmin();
		List<ApplicationSummary> apps = myconfig.getApplications();
		assertNotNull(apps);
	}

	@Test
	public void getApplications_user_granted() {
		asUser(UserFunction.app_list);
		List<ApplicationSummary> apps = myconfig.getApplications();
		assertNotNull(apps);
	}

	@Test(expected = AccessDeniedException.class)
	public void getApplications_user_not_granted() {
		asUser();
		myconfig.getApplications();
	}

	@Test
	public void createApplication_admin() {
		asAdmin();
		ApplicationSummary app = myconfig.createApplication("xxx1");
		assertNotNull(app);
	}

	@Test
	public void createApplication_user_granted() {
		asUser(UserFunction.app_create);
		ApplicationSummary app = myconfig.createApplication("xxx2");
		assertNotNull(app);
	}

	@Test(expected = AccessDeniedException.class)
	public void createApplication_user_not_granted() {
		asUser();
		myconfig.createApplication("xxx");
	}

	@Test
	public void deleteApplication_admin() {
		asAdmin();
		Ack ack = myconfig.deleteApplication(10);
		assertFalse(ack.isSuccess());
	}

	@Test
	public void deleteApplication_user_granted() {
		asUser(10, AppFunction.app_delete);
		Ack ack = myconfig.deleteApplication(10);
		assertFalse(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void deleteApplication_user_not_granted() {
		asUser(11, AppFunction.app_delete);
		myconfig.deleteApplication(10);
	}

	//
	// TODO ApplicationConfiguration getApplicationConfiguration(int id);
	//
	// TODO Ack createVersion(int id, String name);
	//
	// TODO Ack deleteVersion(int id, String name);
	//
	// TODO Ack createEnvironment(int id, String name);
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
	//
	// TODO VersionConfiguration getVersionConfiguration(int application, String
	// version);
	//
	// TODO EnvironmentConfiguration getEnvironmentConfiguration(int
	// application,
	// String environment);
	//
	// TODO KeyConfiguration getKeyConfiguration(int application, String key);
	//
	// TODO Ack updateConfiguration(int application, ConfigurationUpdates
	// updates);
	//
	// TODO Ack updateKey(int application, String name, String description);

	// TODO String getKey(String application, String version, String
	// environment,
	// String key);
	//
	// TODO ConfigurationSet getEnv(String application, String version, String
	// environment);
}
