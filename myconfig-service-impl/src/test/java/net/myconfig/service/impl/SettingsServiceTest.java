package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.ApplicationSettings;
import net.myconfig.service.api.AuditSettings;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.exception.AuditRetentionDaysMustBeDefinedException;

import org.junit.Before;
import org.junit.Test;

public class SettingsServiceTest {

	private ConfigurationService configurationService;
	private SettingsServiceImpl settingsService;

	@Before
	public void init() {
		configurationService = mock(ConfigurationService.class);
		settingsService = new SettingsServiceImpl(configurationService);
	}

	@Test
	public void getApplicationSettings() {
		when(configurationService.getParameter(ConfigurationKey.APP_NAME)).thenReturn("AppName");
		when(configurationService.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS)).thenReturn("AppReplytoAddress");
		when(configurationService.getParameter(ConfigurationKey.APP_REPLYTO_NAME)).thenReturn("AppReplytoName");
		ApplicationSettings settings = settingsService.getApplicationSettings();
		assertNotNull(settings);
		assertEquals("AppName", settings.getName());
		assertEquals("AppReplytoAddress", settings.getReplyToAddress());
		assertEquals("AppReplytoName", settings.getReplyToName());
	}

	@Test
	public void getAuditSettings() {
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("20");
		AuditSettings settings = settingsService.getAuditSettings();
		assertNotNull(settings);
		assertEquals(20, settings.getRetentionDays());
	}
	
	@Test
	public void setApplicationSettings() {
		settingsService.setApplicationSettings("MyName", "MyReplyAddress", "MyReplyName");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_NAME, "MyName");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, "MyReplyAddress");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_REPLYTO_NAME, "MyReplyName");
	}
	
	@Test
	public void setAuditRetentionDays() {
		settingsService.setAuditRetentionDays(10);
		verify(configurationService, times(1)).setParameter(ConfigurationKey.AUDIT_RETENTION_DAYS, "10");
	}
	
	@Test(expected = AuditRetentionDaysMustBeDefinedException.class)
	public void setAuditRetentionDays_zero() {
		settingsService.setAuditRetentionDays(0);
	}
	
	@Test(expected = AuditRetentionDaysMustBeDefinedException.class)
	public void setAuditRetentionDays_negative() {
		settingsService.setAuditRetentionDays(-1);
	}

}
