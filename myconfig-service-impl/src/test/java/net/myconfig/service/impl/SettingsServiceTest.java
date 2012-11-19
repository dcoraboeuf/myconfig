package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.ApplicationSettings;
import net.myconfig.service.api.AuditSettings;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.exception.AuditRetentionDaysMustBeDefinedException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public class SettingsServiceTest {

	private ConfigurationService configurationService;
	private SettingsServiceImpl settingsService;
	private SecuritySelector securitySelector;

	@Before
	public void init() {
		configurationService = mock(ConfigurationService.class);
		securitySelector = mock(SecuritySelector.class);
		settingsService = new SettingsServiceImpl(configurationService, securitySelector);
	}

	protected void asAdmin(boolean admin) {
		when(securitySelector.isAdmin(any(Authentication.class))).thenReturn(admin);
	}

	@Test
	public void getApplicationSettings() {
		asAdmin(true);
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
		asAdmin(true);
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("20");
		AuditSettings settings = settingsService.getAuditSettings();
		assertNotNull(settings);
		assertEquals(20, settings.getRetentionDays());
	}

	@Test
	public void setApplicationSettings() {
		asAdmin(true);
		settingsService.setApplicationSettings("MyName", "MyReplyAddress", "MyReplyName");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_NAME, "MyName");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, "MyReplyAddress");
		verify(configurationService, times(1)).setParameter(ConfigurationKey.APP_REPLYTO_NAME, "MyReplyName");
	}

	@Test
	public void setAuditRetentionDays() {
		asAdmin(true);
		settingsService.setAuditRetentionDays(10);
		verify(configurationService, times(1)).setParameter(ConfigurationKey.AUDIT_RETENTION_DAYS, "10");
	}

	@Test(expected = AuditRetentionDaysMustBeDefinedException.class)
	public void setAuditRetentionDays_zero() {
		asAdmin(true);
		settingsService.setAuditRetentionDays(0);
	}

	@Test(expected = AuditRetentionDaysMustBeDefinedException.class)
	public void setAuditRetentionDays_negative() {
		asAdmin(true);
		settingsService.setAuditRetentionDays(-1);
	}

	@Test(expected = AccessDeniedException.class)
	public void setApplicationSettings_non_admin() {
		asAdmin(false);
		settingsService.setApplicationSettings("MyName", "MyReplyAddress", "MyReplyName");
	}

	@Test(expected = AccessDeniedException.class)
	public void setAuditRetentionDays_non_admin() {
		asAdmin(false);
		settingsService.setAuditRetentionDays(10);
		verify(configurationService, times(1)).setParameter(ConfigurationKey.AUDIT_RETENTION_DAYS, "10");
	}

}
