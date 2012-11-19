package net.myconfig.service.api;

public interface SettingsService {

	ApplicationSettings getApplicationSettings();

	AuditSettings getAuditSettings();

	void setApplicationSettings(String name, String replytoAddress, String replytoName);

	void setAuditRetentionDays(int retentionDays);

}
