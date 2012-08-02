package net.myconfig.service.security;

import net.myconfig.service.api.security.UserToken;

public interface SecurityService {

	UserToken getUserToken(String username, String password);

}
