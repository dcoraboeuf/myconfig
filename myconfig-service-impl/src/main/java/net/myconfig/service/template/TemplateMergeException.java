package net.myconfig.service.template;

import net.myconfig.service.exception.CoreException;

public class TemplateMergeException extends CoreException {

	public TemplateMergeException(String templateId, Exception ex) {
		super(ex, templateId, ex);
	}

}
