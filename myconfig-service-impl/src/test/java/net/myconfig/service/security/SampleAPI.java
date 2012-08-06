package net.myconfig.service.security;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserGrant;

public interface SampleAPI {
	
	void no_constraint();
	
	@UserGrant(UserFunction.app_list)
	void user_call();
	
	void app_call(int application);

}
