package net.myconfig.web.renderer;

import net.myconfig.service.exception.CoreException;

public class TemplateNotFoundException extends CoreException {

	public TemplateNotFoundException(String templateName) {
		super (templateName);
	}

}
