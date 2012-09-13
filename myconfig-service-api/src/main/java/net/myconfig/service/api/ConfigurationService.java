package net.myconfig.service.api;

public interface ConfigurationService {

	String SECURITY_MODE = "security.mode";
	String SECURITY_MODE_DEFAULT = "none";

	String APP_NAME = "app.name";
	String APP_NAME_DEFAULT = "myconfig";
	
	String APP_REPLYTO_ADDRESS = "app.replyto.address";
	String APP_REPLYTO_ADDRESS_DEFAULT = "noreply@myconfig.net";

	String APP_REPLYTO_NAME = "app.replyto.name";
	String APP_REPLYTO_NAME_DEFAULT = "the myconfig team";

	String getParameter(String name, String defaultValue);

	void setParameter(String name, String value);

}
