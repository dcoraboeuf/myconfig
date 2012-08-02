package net.myconfig.service.security.manager;

import net.myconfig.service.api.security.SecurityManagement;

public abstract class AbstractSecurityManagement implements SecurityManagement {

	private final String id;

	public AbstractSecurityManagement(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
