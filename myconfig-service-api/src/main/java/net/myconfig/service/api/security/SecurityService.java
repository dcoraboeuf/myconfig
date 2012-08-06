package net.myconfig.service.api.security;

import java.util.List;

import net.myconfig.service.model.UserSummary;

public interface SecurityService {

	UserToken getUserToken(String username, String password);

	List<UserSummary> getUserList();

}
