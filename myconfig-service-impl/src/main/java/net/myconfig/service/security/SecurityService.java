package net.myconfig.service.security;

import java.util.List;

import net.myconfig.core.MyConfigFunctions;

public interface SecurityService {

	List<MyConfigFunctions> getUserFunctions(String username);

	User getUser(String username, String password);

}
