package net.myconfig.web.support.fm.security;

import java.util.List;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.User;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateModelException;

public class FnSecUserName extends AbstractFnSec {

	@Autowired
	public FnSecUserName(SecuritySelector selector) {
		super(selector);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 0, "No argument is needed");
		// User token
		User user = getUser();
		if (user != null) {
			return user.getDisplayName();
		} else {
			return "";
		}
	}

}
