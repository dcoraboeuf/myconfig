package net.myconfig.web.support.fm.security;

import java.util.List;

import net.myconfig.service.api.security.SecuritySelector;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateModelException;

public class FnSecAllowLogin extends AbstractFnSec {

	@Autowired
	public FnSecAllowLogin(SecuritySelector selector) {
		super(selector);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 0, "No argument is needed");
		// Test
		return selector.getSecurityManagement().allowLogin();
	}

}
