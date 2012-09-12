package net.myconfig.service.template;

import java.io.IOException;

import net.myconfig.service.exception.CoreException;

public class TemplateNotFoundException extends CoreException {

	public TemplateNotFoundException(String templateId, IOException ex) {
		super(ex, templateId, ex);
	}

}
