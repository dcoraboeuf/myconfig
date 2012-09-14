package net.myconfig.web.support.fm.security;

import java.util.List;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserProfile;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateModelException;

public class FnSecIsAdmin extends AbstractFnSec {

	@Autowired
	public FnSecIsAdmin(SecuritySelector selector) {
		super(selector);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.isTrue(list.size() == 0, "No argument is needed");
		// Test
		UserProfile profile = getUserToken();
		return profile != null && profile.isAdmin();
	}

}
