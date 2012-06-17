package net.myconfig.web.rest;

import net.myconfig.service.exception.CoreException;

public class ConfigurationModeNotFoundException extends CoreException {

	public ConfigurationModeNotFoundException(String mode) {
		super (mode);
	}

}
