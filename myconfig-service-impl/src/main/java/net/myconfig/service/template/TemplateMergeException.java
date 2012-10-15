package net.myconfig.service.template;

import net.myconfig.core.CoreException;

public class TemplateMergeException extends CoreException {

	public TemplateMergeException(String templateId, Exception ex) {
		super(ex, templateId, ex);
	}

}
