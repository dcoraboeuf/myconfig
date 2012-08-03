package net.myconfig.web.support.fm.security;

import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecuritySelector;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnSecChkUserFn implements TemplateMethodModel {

	private final SecuritySelector selector;

	@Autowired
	public FnSecChkUserFn(SecuritySelector selector) {
		this.selector = selector;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 1, "List of arguments must contain one argument only");
		// Gets the function
		UserFunction fn = UserFunction.valueOf((String) list.get(0));
		// Gets the current authentication
		Authentication authentication = getAuthentication();
		// Test
		return selector.getSecurityManagement().hasUserFunction(authentication, fn);
	}

	protected Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return null;
		} else {
			return context.getAuthentication();
		}
	}

}
