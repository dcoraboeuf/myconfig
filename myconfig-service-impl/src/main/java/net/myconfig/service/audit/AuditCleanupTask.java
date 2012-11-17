package net.myconfig.service.audit;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuditCleanupTask {

	private final Logger logger = LoggerFactory.getLogger(AuditCleanupTask.class);
	
	private static final long DELAY = 60 * 60 * 1000; // 1 hour 

	private final ConfigurationService configurationService;
	private final EventService eventService;

	@Autowired
	public AuditCleanupTask(ConfigurationService configurationService, EventService eventService) {
		this.configurationService = configurationService;
		this.eventService = eventService;
	}

	/**
	 * Tests for clean-up every hour
	 */
	@Scheduled(fixedDelay = DELAY)
	public void cleanupTrigger() {
		logger.info("[audit-cleanup] Clean-up trigerring");
		// Gets the retention period
		String retentionDaysParam = configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS);
		int retentionDays;
		try {
			retentionDays = Integer.parseInt(retentionDaysParam);
			// Check
			if (retentionDays > 0) {
				logger.info("[audit-cleanup] Retention days: {}", retentionDays);
				// Launches the clean-up
				int count = eventService.clean(retentionDays);
				// Logging
				logger.info("[audit-cleanup] {} audit events have been cleaned", count);
			} else {
				logger.error("Invalid number of retention days: {}", retentionDaysParam);
			}
		} catch (NumberFormatException ex) {
			logger.error("Cannot parse the number of retention days: {}", retentionDaysParam);
		}
	}

}
