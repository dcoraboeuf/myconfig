package net.myconfig.service.security;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserGrant;

public interface SampleAPI {
	
	void no_constraint();
	
	@UserGrant(UserFunction.app_list)
	void user_call();
	
	void app_call(int application);
	
	void env_call_missing_param(int application);
	
	void env_call_ok(int application, String environment);
	
	void env_call_no_annotation(int application, String environment);
	
	void env_call_too_much(int application, String environment, String other);

}
