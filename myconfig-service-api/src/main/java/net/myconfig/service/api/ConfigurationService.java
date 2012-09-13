package net.myconfig.service.api;

public interface ConfigurationService {

	String getParameter(ConfigurationKey configurationKey);

	void setParameter(ConfigurationKey configurationKey, String value);

}
