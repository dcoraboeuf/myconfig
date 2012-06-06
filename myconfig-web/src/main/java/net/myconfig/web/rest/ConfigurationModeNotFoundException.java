package net.myconfig.web.rest;

import net.myconfig.core.CoreException;

public class ConfigurationModeNotFoundException extends CoreException {

	public ConfigurationModeNotFoundException(String mode) {
		super (mode);
	}

}
