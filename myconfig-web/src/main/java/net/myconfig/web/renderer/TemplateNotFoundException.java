package net.myconfig.web.renderer;

import net.myconfig.core.CoreException;

public class TemplateNotFoundException extends CoreException {

	public TemplateNotFoundException(String templateName) {
		super (templateName);
	}

}
