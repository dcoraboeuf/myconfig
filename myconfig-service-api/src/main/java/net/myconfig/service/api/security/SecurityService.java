package net.myconfig.service.api.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.UserSummaries;

public interface SecurityService {

	UserSummaries getUserList();

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

	void updateUserData(String password, String displayName, String email);

	Ack appFunctionAdd(String application, String user, AppFunction fn);

	Ack appFunctionRemove(String application, String user, AppFunction fn);

	Ack envFunctionAdd(String application, String user, String environment, EnvFunction fn);

	Ack envFunctionRemove(String application, String user, String environment, EnvFunction fn);

}
