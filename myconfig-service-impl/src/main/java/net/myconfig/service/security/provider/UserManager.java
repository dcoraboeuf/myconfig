package net.myconfig.service.security.provider;

import net.myconfig.core.model.Ack;

public interface UserManager {

	void checkUserConfirm(String name, String token);

	void checkUserForgotten(String name, String token);

	void checkUserReset(String name, String token);

	void checkUserChangePassword(String name, String token);

	void userReset(String name, String token, String password);

	void userChangePassword(String name, String token, String oldPassword, String newPassword);

	void userConfirm(String name, String token, String password);

	void userForgottenSet(String name, String token, String password);

	void userChangePassword();

	void userReset(String name);

	Ack userForgotten(String email);

}
