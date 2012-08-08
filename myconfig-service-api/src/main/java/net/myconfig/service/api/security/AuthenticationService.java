package net.myconfig.service.api.security;

public interface AuthenticationService {

	UserProfile getUserToken(String username, String password);

}
