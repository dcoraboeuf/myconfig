package net.myconfig.service.cache;

public interface CacheNames {

	/**
	 * Key: configuration key
	 */
	String CONFIGURATION = "configuration";
	
	/**
	 * Key: user name
	 */
	String USER_FUNCTIONS = "userFunctions";
	
	/**
	 * Key: user name + function
	 */
	String USER_FUNCTION = "userFunction";

	/**
	 * Key: user name + application id + function
	 */
	String APP_FUNCTION = "appFunction";

	/**
	 * Key: user name + application id + environment + function
	 */
	// FIXME String ENV_FUNCTION = "envFunction";

}
