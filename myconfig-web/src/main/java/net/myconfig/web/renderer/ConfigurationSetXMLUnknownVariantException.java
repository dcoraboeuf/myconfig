package net.myconfig.web.renderer;

import net.myconfig.service.exception.CoreException;

public class ConfigurationSetXMLUnknownVariantException extends CoreException {

	public ConfigurationSetXMLUnknownVariantException(String variant) {
		super (variant);
	}

}
