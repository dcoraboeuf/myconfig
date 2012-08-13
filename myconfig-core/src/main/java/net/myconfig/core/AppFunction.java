package net.myconfig.core;

public enum AppFunction {
	
	/**
	 * Access to this application and its versions and keys
	 */
	app_view, 
	/**
	 * Deletion of an existing application
	 */
	app_delete, 
	/**
	 * Granting rights for users for this application
	 */
	app_users,
	/**
	 * Management of versions, keys and environments
	 */
	app_config,
	/**
	 * Management of the matrix between keys and versions
	 */
	app_matrix; 

}
