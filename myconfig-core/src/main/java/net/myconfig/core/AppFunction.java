package net.myconfig.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum AppFunction {

	/**
	 * Management of versions, keys and environments
	 */
	app_config,
	/**
	 * Management of the matrix between keys and versions
	 */
	app_matrix,
	/**
	 * Access to this application and its versions and keys
	 */
	app_view(app_config, app_matrix),
	/**
	 * Deletion of an existing application
	 */
	app_delete,
	/**
	 * Creation of an environment for the application
	 */
	app_envcreate,
	/**
	 * Granting rights for users for this application
	 */
	app_users;

	private final Set<AppFunction> parents;

	private AppFunction() {
		parents = Collections.<AppFunction> emptySet();
	}

	private AppFunction(AppFunction... fns) {
		parents = Collections.unmodifiableSet(new HashSet<AppFunction>(Arrays.asList(fns)));
	}

	public boolean hasParents() {
		return !parents.isEmpty();
	}

	public Iterable<AppFunction> getParents() {
		return parents;
	}

}
