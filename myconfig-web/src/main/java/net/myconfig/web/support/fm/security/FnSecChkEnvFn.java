package net.myconfig.web.support.fm.security;

import java.util.List;

import net.myconfig.core.EnvFunction;
import net.myconfig.service.api.security.SecuritySelector;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import freemarker.template.TemplateModelException;

public class FnSecChkEnvFn extends AbstractFnSec {

	@Autowired
	public FnSecChkEnvFn(SecuritySelector selector) {
		super(selector);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 3, "List of arguments must contain one argument only");
		// Gets the application ID
		int application = Integer.parseInt((String) list.get(0), 10);
		// Gets the environment
		String environment = (String) list.get(1);
		// Gets the function
		EnvFunction fn = EnvFunction.valueOf((String) list.get(2));
		// Gets the current authentication
		Authentication authentication = getAuthentication();
		// Test
		return selector.hasEnvironmentFunction(authentication, application, environment, fn);
	}

}
