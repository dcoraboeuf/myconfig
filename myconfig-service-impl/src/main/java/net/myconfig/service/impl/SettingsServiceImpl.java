package net.myconfig.service.impl;

import net.myconfig.service.api.ApplicationSettings;
import net.myconfig.service.api.AuditSettings;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.SettingsService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.exception.AuditRetentionDaysMustBeDefinedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsServiceImpl implements SettingsService {

	private final ConfigurationService configurationService;
	private final SecuritySelector securitySelector;

	@Autowired
	public SettingsServiceImpl(ConfigurationService configurationService, SecuritySelector securitySelector) {
		this.configurationService = configurationService;
		this.securitySelector = securitySelector;
	}

	@Override
	public ApplicationSettings getApplicationSettings() {
		String appName = configurationService.getParameter(ConfigurationKey.APP_NAME);
		String appReplyToAddress = configurationService.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS);
		String appReplyToName = configurationService.getParameter(ConfigurationKey.APP_REPLYTO_NAME);
		return new ApplicationSettings(appName, appReplyToAddress, appReplyToName);
	}

	@Override
	public AuditSettings getAuditSettings() {
		return new AuditSettings(Integer.parseInt(configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS), 10));
	}

	@Override
	@Transactional
	public void setApplicationSettings(String name, String replytoAddress, String replytoName) {
		SecurityUtils.checkIsAdmin(securitySelector);
		configurationService.setParameter(ConfigurationKey.APP_NAME, name);
		configurationService.setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, replytoAddress);
		configurationService.setParameter(ConfigurationKey.APP_REPLYTO_NAME, replytoName);
	}

	@Override
	@Transactional
	public void setAuditRetentionDays(int retentionDays) {
		SecurityUtils.checkIsAdmin(securitySelector);
		if (retentionDays < 1) {
			throw new AuditRetentionDaysMustBeDefinedException();
		} else {
			configurationService.setParameter(ConfigurationKey.AUDIT_RETENTION_DAYS, String.valueOf(retentionDays));
		}
	}

}
