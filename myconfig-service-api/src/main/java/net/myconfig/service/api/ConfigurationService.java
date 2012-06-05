package net.myconfig.service.api;

public interface ConfigurationService {

	String getDBDriver();

	String getDBURL();

	String getDBUser();

	String getDBPassword();

	int getDBPoolInitial();

	int getDBPoolMax();

}
