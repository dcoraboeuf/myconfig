package net.myconfig.service.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.AppGrantParam;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;

public class SampleImpl implements SampleAPI {

	@Override
	public void user_call() {
	}

	@Override
	@AppGrant(AppFunction.app_view)
	public void app_call(@AppGrantParam String application) {
	}

	@Override
	public void no_constraint() {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_missing_param(@AppGrantParam String application) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_ok(@AppGrantParam String application, @EnvGrantParam String environment) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_no_annotation(@AppGrantParam String application, String environment) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_too_much(@AppGrantParam String application, @EnvGrantParam String environment, @EnvGrantParam String other) {
	}

}
