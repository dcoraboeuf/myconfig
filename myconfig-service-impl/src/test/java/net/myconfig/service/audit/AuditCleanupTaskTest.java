package net.myconfig.service.audit;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.EventService;

import org.junit.Before;
import org.junit.Test;

public class AuditCleanupTaskTest {

	private ConfigurationService configurationService;
	private EventService eventService;
	private AuditCleanupTask task;

	@Before
	public void before() {
		configurationService = mock(ConfigurationService.class);
		eventService = mock(EventService.class);
		task = new AuditCleanupTask(configurationService, eventService);
	}

	@Test
	public void invalidRetentionDays() {
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("m");
		task.cleanupTrigger();
		verify(eventService, never()).clean(anyInt());
	}

	@Test
	public void negativeRetentionDays() {
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("-2");
		task.cleanupTrigger();
		verify(eventService, never()).clean(anyInt());
	}

	@Test
	public void zeroRetentionDays() {
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("0");
		task.cleanupTrigger();
		verify(eventService, never()).clean(anyInt());
	}

	@Test
	public void retentionDays() {
		when(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS)).thenReturn("10");
		task.cleanupTrigger();
		verify(eventService, times(1)).clean(10);
	}

}
