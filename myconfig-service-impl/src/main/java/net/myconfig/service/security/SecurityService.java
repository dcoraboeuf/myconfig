package net.myconfig.service.security;

import java.util.List;

import net.myconfig.core.UserFunction;

public interface SecurityService {

	List<UserFunction> getUserFunctions(User user);

	User getUser(String username, String password);

}
