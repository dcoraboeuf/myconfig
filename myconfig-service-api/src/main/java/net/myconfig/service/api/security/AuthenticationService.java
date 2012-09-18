package net.myconfig.service.api.security;

public interface AuthenticationService {

	User getUserToken(String username, String password);

}
