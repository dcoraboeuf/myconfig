package net.myconfig.service.api;

public interface ConfigurationService {

	String SECURITY_MODE = "security.mode";
	String SECURITY_MODE_DEFAULT = "none";

	String getParameter(String name, String defaultValue);

	void setParameter(String name, String value);

}
