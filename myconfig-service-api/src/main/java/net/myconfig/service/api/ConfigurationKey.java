package net.myconfig.service.api;

public enum ConfigurationKey {

	SECURITY_MODE("security.mode", "none"),
	APP_NAME("app.name", "myconfig"),
	APP_REPLYTO_ADDRESS("app.replyto.address", "noreply@myconfig.net"),
	APP_REPLYTO_NAME("app.replyto.name", "the myconfig team"),
	AUDIT_RETENTION_DAYS("audit.retention.days", "60"),
	LDAP_SERVER("ldap.server", "");

	private final String key;
	private final String defaultValue;

	private ConfigurationKey(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefault() {
		return defaultValue;
	}

}
