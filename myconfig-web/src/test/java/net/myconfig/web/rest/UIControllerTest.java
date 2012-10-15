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
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.test.support.ApplicationSummaryBuilder;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;

public class UIControllerTest {

	private static final String DESCRIPTION = "description";
	private static final String APP = "myapp";
	private static final String VERSION = "1.0";
	private static final String ENV = "ENV";
	private static final String KEY = "mykey";

	private UIController ui;
	private MyConfigService service;
	private SecurityService security;

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
		// OK
		ui = new UIController(strings, errorHandler, service, security);
	}

	@Test
	public void application_list() {
		ApplicationSummaries expectedApplications = new ApplicationSummaries(Arrays.asList(ApplicationSummaryBuilder.create(1, "app1").build(), ApplicationSummaryBuilder.create(2, "app2").build()));
		when(service.getApplications()).thenReturn(expectedApplications);
		ApplicationSummaries actualApplications = ui.applications();
		assertSame(expectedApplications, actualApplications);
	}

	@Test
	public void application_create() {
		ApplicationSummary expectedApp = ApplicationSummaryBuilder.create(1, APP).build();
		when(service.createApplication(APP)).thenReturn(expectedApp);
		ApplicationSummary actualApp = ui.applicationCreate(APP);
		assertSame(expectedApp, actualApp);
	}

	@Test
	public void application_delete() {
		when(service.deleteApplication(1)).thenReturn(Ack.OK);
		Ack ack = ui.applicationDelete(1);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void application_configuration() {
		ApplicationConfiguration expectedConf = new ApplicationConfiguration(1, APP, Collections.<VersionSummary> emptyList(), Collections.<EnvironmentSummary> emptyList(),
				Collections.<KeySummary> emptyList());
		when(service.getApplicationConfiguration(1)).thenReturn(expectedConf);
		ApplicationConfiguration actualConf = ui.applicationConfiguration(1);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void version_configuration() {
		VersionConfiguration expectedConf = new VersionConfiguration(1, APP, VERSION, null, null, Collections.<Key> emptyList(), Collections.<IndexedValues<String>> emptyList());
		when(service.getVersionConfiguration(1, VERSION)).thenReturn(expectedConf);
		VersionConfiguration actualConf = ui.versionConfiguration(1, VERSION);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void version_create() {
		when(service.createVersion(1, VERSION)).thenReturn(Ack.OK);
		Ack ack = ui.versionCreate(1, VERSION);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void version_delete() {
		when(service.deleteVersion(1, VERSION)).thenReturn(Ack.OK);
		Ack ack = ui.versionDelete(1, VERSION);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void environment_configuration() {
		EnvironmentConfiguration expectedConf = new EnvironmentConfiguration(1, APP, ENV, null, null, Collections.<Key> emptyList(), Collections.<IndexedValues<ConditionalValue>> emptyList());
		when(service.getEnvironmentConfiguration(1, ENV)).thenReturn(expectedConf);
		EnvironmentConfiguration actualConf = ui.environmentConfiguration(1, ENV);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void key_configuration() {
		KeyConfiguration expectedConf = new KeyConfiguration(1, APP, new Key(KEY, ""), null, null, Collections.<Version> emptyList(), Collections.<IndexedValues<String>> emptyList());
		when(service.getKeyConfiguration(1, KEY)).thenReturn(expectedConf);
		KeyConfiguration actualConf = ui.keyConfiguration(1, KEY);
		assertSame(expectedConf, actualConf);
	}

	@Test
	public void environment_create() {
		when(service.createEnvironment(1, ENV)).thenReturn(Ack.OK);
		Ack ack = ui.environmentCreate(1, ENV);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void environment_delete() {
		when(service.deleteEnvironment(1, ENV)).thenReturn(Ack.OK);
		Ack ack = ui.environmentDelete(1, ENV);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void key_create() {
		when(service.createKey(1, KEY, DESCRIPTION)).thenReturn(Ack.OK);
		Ack ack = ui.keyCreate(1, KEY, DESCRIPTION);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void key_delete() {
		when(service.deleteKey(1, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyDelete(1, KEY);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void key_update() {
		when(service.updateKey(1, KEY, "New description")).thenReturn(Ack.OK);
		Ack ack = ui.keyUpdate(1, KEY, "New description");
		assertTrue(ack.isSuccess());
	}

	@Test
	public void update_configuration() {
		ConfigurationUpdates updates = new ConfigurationUpdates(Collections.<ConfigurationUpdate>emptyList());
		when(service.updateConfiguration(1, updates)).thenReturn(Ack.OK);
		Ack ack = ui.updateConfiguration(1, updates);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void matrix() {
		MatrixConfiguration expectedMatrix = new MatrixConfiguration(
				1, APP, 
				Collections.<MatrixVersionConfiguration>emptyList(),
				Collections.<Key>emptyList()); 
		when(service.keyVersionConfiguration(1)).thenReturn(expectedMatrix);
		MatrixConfiguration actualMatrix = ui.keyVersionConfiguration(1);
		assertSame (expectedMatrix, actualMatrix);
	}
	
	@Test
	public void matrix_add() {
		when(service.addKeyVersion(1, VERSION, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyVersionAdd(1, VERSION, KEY);
		assertTrue(ack.isSuccess());
	}
	
	@Test
	public void matrix_remove() {
		when(service.removeKeyVersion(1, VERSION, KEY)).thenReturn(Ack.OK);
		Ack ack = ui.keyVersionRemove(1, VERSION, KEY);
		assertTrue(ack.isSuccess());
	}
}
