package net.myconfig.web.renderer;

import net.myconfig.service.exception.CoreException;

public class ConfigurationSetJSONUnknownVariantException extends CoreException {

	public ConfigurationSetJSONUnknownVariantException(String variant) {
		super (variant);
	}

}
