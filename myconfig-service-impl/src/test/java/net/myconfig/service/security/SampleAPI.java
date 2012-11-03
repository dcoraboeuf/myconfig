package net.myconfig.service.security;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserGrant;

public interface SampleAPI {
	
	void no_constraint();
	
	@UserGrant(UserFunction.app_list)
	void user_call();
	
	void app_call(String application);
	
	void env_call_missing_param(String application);
	
	void env_call_ok(String application, String environment);
	
	void env_call_no_annotation(String application, String environment);
	
	void env_call_too_much(String application, String environment, String other);

}
