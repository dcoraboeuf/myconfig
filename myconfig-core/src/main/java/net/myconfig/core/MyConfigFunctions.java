package net.myconfig.core;

public enum MyConfigFunctions {
	
	/**
	 * Set-up of the security
	 */
	security_setup,
	/**
	 * Managements of the users
	 */
	security_users,
	/**
	 * Creation of a new application
	 */
	app_create,
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
	 * Creation of a new version
	 */
	version_create,
	/**
	 * Deletion of a version
	 */
	version_delete,
	/**
	 * Creation of an environment
	 */
	env_create,
	/**
	 * Deletion of an environment
	 */
	env_delete, 
	/**
	 * Creation of a key
	 */
	key_create,
	/**
	 * Deletion of a key
	 */
	key_delete, 
	/**
	 * Update the description of a key
	 */
	key_update,
	/**
	 * Management of the matrix between keys and versions
	 */
	matrix,
	/**
	 * Access to this environment for this application
	 */
	env_view,
	/**
	 * Configuration for this environment, for all versions and keys
	 */
	env_config; 

}
