package net.myconfig.service.api.security;

import net.myconfig.core.model.Ack;

public interface UserProvider {
	
	String getId();

	User getUser(String username, String password);

	Ack create(String name, String displayName, String email);

}
