package net.myconfig.core;

public enum EnvFunction {
	/**
	 * Access to this environment for this application
	 */
	env_view,
	/**
	 * Configuration for this environment, for all versions and keys
	 */
	env_config,
	/**
	 * Deletion of this environment
	 */
	env_delete,
	/**
	 * Management of the users for this environment
	 */
	env_users; 

}
