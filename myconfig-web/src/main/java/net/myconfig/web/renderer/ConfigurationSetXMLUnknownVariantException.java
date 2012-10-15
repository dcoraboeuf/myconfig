package net.myconfig.web.renderer;

import net.myconfig.core.CoreException;

public class ConfigurationSetXMLUnknownVariantException extends CoreException {

	public ConfigurationSetXMLUnknownVariantException(String variant) {
		super (variant);
	}

}
