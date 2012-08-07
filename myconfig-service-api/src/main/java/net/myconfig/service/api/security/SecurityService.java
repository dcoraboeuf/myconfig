package net.myconfig.service.api.security;

import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;

public interface SecurityService {

	UserToken getUserToken(String username, String password);

	List<UserSummary> getUserList();

	Ack userCreate(String name);

	Ack userDelete(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	String getSecurityMode();

	void setSecurityMode(String mode);

}
