package net.myconfig.web.settings;

import net.myconfig.service.exception.CoreException;

public class SettingParameterMultipleException extends CoreException {

	public SettingParameterMultipleException(String name) {
		super(name);
	}

}
