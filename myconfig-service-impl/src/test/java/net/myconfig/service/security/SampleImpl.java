package net.myconfig.service.security;

import net.myconfig.core.AppFunction;
import net.myconfig.service.api.security.AppGrant;

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

}
