package net.myconfig.service.security;

import java.util.List;

import net.myconfig.core.EnvFunction;

public interface SecurityService {

	List<EnvFunction> getUserFunctions(String username);

	User getUser(String username, String password);

}
