package net.myconfig.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum EnvFunction {
	/**
	 * Configuration for this environment, for all versions and keys
	 */
	env_config,
	/**
	 * Access to this environment for this application
	 */
	env_view(env_config),
	/**
	 * Deletion of this environment
	 */
	env_delete,
	/**
	 * Management of the users for this environment
	 */
	env_users; 

	private final Set<EnvFunction> parents;

	private EnvFunction() {
		parents = Collections.<EnvFunction> emptySet();
	}

	private EnvFunction(EnvFunction... fns) {
		parents = Collections.unmodifiableSet(new HashSet<EnvFunction>(Arrays.asList(fns)));
	}

	public boolean hasParents() {
		return !parents.isEmpty();
	}

	public Iterable<EnvFunction> getParents() {
		return parents;
	}

}
