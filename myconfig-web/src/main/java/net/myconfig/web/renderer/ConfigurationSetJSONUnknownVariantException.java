package net.myconfig.web.renderer;

import net.myconfig.core.CoreException;

public class ConfigurationSetJSONUnknownVariantException extends CoreException {

	public ConfigurationSetJSONUnknownVariantException(String variant) {
		super (variant);
	}

}
