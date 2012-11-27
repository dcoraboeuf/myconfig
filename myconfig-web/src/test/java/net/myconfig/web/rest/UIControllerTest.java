package net.myconfig.web.rest;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.myconfig.core.model.ConditionalValue;
import net.myconfig.core.model.ConfigurationUpdate;
import net.myconfig.core.model.ConfigurationUpdates;
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
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserManager;
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.test.support.ApplicationSummaryBuilder;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;

public class UIControllerTest {

	private static final String DESCRIPTION = "description";
	private static final String APP = "APP";
	private static final String APP_NAME = "myapp";
	private static final String VERSION = "1.0";
	private static final String ENV = "ENV";
	private static final String KEY = "mykey";

	private UIController ui;
	private MyConfigService service;
	private SecurityService security;
	private UserManager userManager;

	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
		// Strings
		Strings strings = new Strings();
		// Error handler
		ErrorHandler errorHandler = mock(ErrorHandler.class);
		// Service(s)
		service = mock(MyConfigService.class);
		security = mock(SecurityService.class);
		userManager = mock(UserManager.class);
		// OK
		ui = new UIController(strings, errorHandler, service, security, userManager);
	}

	@Test
	public void application_list() {
		ApplicationSummaries expectedApplications = new ApplicationSummaries(Arrays.asList(ApplicationSummaryBuilder.create("APP", "app1").build(), ApplicationSummaryBuilder.create("APP2", "app2").build()));
		when(service.getApplications()).thenReturn(expectedApplications);
		ApplicationSummaries actualApplications = ui.applications();
		assertSame(expectedApplications, actualApplications);
	}

	@Test
	public void application_create() {
		ApplicationSummary expectedApp = ApplicationSummaryBuilder.create(APP, APP_NAME).build();
		when(service.createApplication(APP, APP_NAME)).thenReturn(expectedApp);
		ApplicationSummary actualApp = ui.applicationCreate(APP, APP_NAME);
		assertSame(expectedApp, actualApp);
	}

	@Test
	public void application_delete() {
		when(service.deleteApplication(APP)).thenReturn(Ack.OK);
		Ack ack = ui.applicationDelete(APP);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void application_configuration() {
		ApplicationConfiguration expectedConf = new ApplicationConfiguration(APP, APP_NAME, Collections.<VersionSummary> emptyList(), Collections.<EnvironmentSummary> emptyList(),
				Collections.<KeySummary> emptyList());
		when(service.getApplicationConfiguration(APP)).thenReturn(expectedConf);
		ApplicationConfiguration actualConf = ui.applicationConfiguration(APP);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void version_configuration() {
		VersionConfiguration expectedConf = new VersionConfiguration(APP, APP_NAME, VERSION, null, null, Collections.<Key> emptyList(), Collections.<IndexedValues<String>> emptyList());
		when(service.getVersionConfiguration(APP, VERSION)).thenReturn(expectedConf);
		VersionConfiguration actualConf = ui.versionConfiguration(APP, VERSION);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void version_create() {
		when(service.createVersion(APP, VERSION)).thenReturn(Ack.OK);
		Ack ack = ui.versionCreate(APP, VERSION);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void version_delete() {
		when(service.deleteVersion(APP, VERSION)).thenReturn(Ack.OK);
		Ack ack = ui.versionDelete(APP, VERSION);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void environment_configuration() {
		EnvironmentConfiguration expectedConf = new EnvironmentConfiguration(APP, APP_NAME, ENV, null, null, Collections.<Key> emptyList(), Collections.<IndexedValues<ConditionalValue>> emptyList());
		when(service.getEnvironmentConfiguration(APP, ENV)).thenReturn(expectedConf);
		EnvironmentConfiguration actualConf = ui.environmentConfiguration(APP, ENV);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void key_configuration() {
		KeyConfiguration expectedConf = new KeyConfiguration(APP, APP_NAME, new Key(KEY, "", "plain", null), null, null, Collections.<Version> emptyList(), Collections.<IndexedValues<String>> emptyList());
		when(service.getKeyConfiguration(APP, KEY)).thenReturn(expectedConf);
		KeyConfiguration actualConf = ui.keyConfiguration(APP, KEY);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void environment_create() {
		when(service.createEnvironment(APP, ENV)).thenReturn(Ack.OK);
		Ack ack = ui.environmentCreate(APP, ENV);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void environment_delete() {
		when(service.deleteEnvironment(APP, ENV)).thenReturn(Ack.OK);
		Ack ack = ui.environmentDelete(APP, ENV);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void key_create() {
		when(service.createKey(APP, KEY, DESCRIPTION, null, null)).thenReturn(Ack.OK);
		Ack ack = ui.keyCreate(APP, KEY, DESCRIPTION, null, null);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void key_delete() {
		when(service.deleteKey(APP, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyDelete(APP, KEY);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void key_update() {
		when(service.updateKey(APP, KEY, "New description")).thenReturn(Ack.OK);
		Ack ack = ui.keyUpdate(APP, KEY, "New description");
		assertTrue(ack.isSuccess());
	}

	@Test
	public void update_configuration() {
		ConfigurationUpdates updates = new ConfigurationUpdates(Collections.<ConfigurationUpdate>emptyList());
		when(service.updateConfiguration(APP, updates)).thenReturn(Ack.OK);
		Ack ack = ui.updateConfiguration(APP, updates);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void matrix() {
		MatrixConfiguration expectedMatrix = new MatrixConfiguration(
				APP, APP_NAME, 
				Collections.<MatrixVersionConfiguration>emptyList(),
				Collections.<Key>emptyList()); 
		when(service.keyVersionConfiguration(APP)).thenReturn(expectedMatrix);
		MatrixConfiguration actualMatrix = ui.keyVersionConfiguration(APP);
		assertSame (expectedMatrix, actualMatrix);
	}
	
	@Test
	public void matrix_add() {
		when(service.addKeyVersion(APP, VERSION, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyVersionAdd(APP, VERSION, KEY);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void matrix_remove() {
		when(service.removeKeyVersion(APP, VERSION, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyVersionRemove(APP, VERSION, KEY);
		assertTrue(ack.isSuccess());
	}
}
