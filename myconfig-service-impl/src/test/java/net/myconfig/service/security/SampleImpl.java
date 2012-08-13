package net.myconfig.service.security;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.EnvGrant;
import net.myconfig.service.api.security.EnvGrantParam;

public class SampleImpl implements SampleAPI {

	@Override
	public void user_call() {
	}

	@Override
	@AppGrant(AppFunction.app_view)
	public void app_call(int application) {
	}

	@Override
	public void no_constraint() {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_missing_param(int application) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_ok(int application, @EnvGrantParam String environment) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_no_annotation(int application, String environment) {
	}

	@Override
	@EnvGrant(EnvFunction.env_view)
	public void env_call_too_much(int application, @EnvGrantParam String environment, @EnvGrantParam String other) {
	}

}
