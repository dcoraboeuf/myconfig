package net.myconfig.service.security;

import net.myconfig.service.api.NamedAuthenticationProvider;

public abstract class AbstractNamedAuthenticationProvider implements NamedAuthenticationProvider {

	private final String id;

	public AbstractNamedAuthenticationProvider(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
