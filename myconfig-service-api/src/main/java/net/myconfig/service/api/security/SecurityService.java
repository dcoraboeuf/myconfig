package net.myconfig.service.api.security;

import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;

public interface SecurityService {

	List<UserSummary> getUserList();

	Ack userCreate(String name, String displayName, String email);

	Ack userDelete(String name);

	Ack userFunctionAdd(String name, UserFunction fn);

	Ack userFunctionRemove(String name, UserFunction fn);

	void setSecurityMode(String mode);

	void checkUserConfirm(String name, String token);

	void userConfirm(String name, String token, String password);

	void userChangePassword();

	void checkUserChangePassword(String name, String token);

	void userChangePassword(String name, String token, String oldPassword, String newPassword);

	void checkUserReset(String name, String token);

	void userReset(String name, String token, String password);

	Ack userForgotten(String email);

	void userDisable(String name);

	void userEnable(String name);

	void userReset(String name);

	void checkUserForgotten(String name, String token);

	void userForgottenSet(String name, String token, String password);

}
