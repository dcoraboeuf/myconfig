package net.myconfig.service.api.security;

public interface AuthenticationService {

	UserToken getUserToken(String username, String password);

}
