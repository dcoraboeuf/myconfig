package net.myconfig.service.security.provider;

import net.myconfig.service.api.security.User;

public interface UserProvider {
	
	String getId();

	User getUser(String username, String password);

}
