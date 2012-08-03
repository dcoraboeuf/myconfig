package net.myconfig.web.settings;

import net.myconfig.service.exception.CoreException;

public class SettingParameterMissingException extends CoreException {

	public SettingParameterMissingException(String name) {
		super(name);
	}

}
