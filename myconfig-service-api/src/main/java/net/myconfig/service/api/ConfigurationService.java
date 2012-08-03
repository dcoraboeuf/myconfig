package net.myconfig.service.api;

public interface ConfigurationService {

	String getParameter(String name, String defaultValue);

	void setParameter(String name, String value);

}
