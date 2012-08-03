package net.myconfig.web.support.fm.security;

import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import freemarker.template.TemplateMethodModel;

public abstract class AbstractFnSec implements TemplateMethodModel {

	protected final SecuritySelector selector;

	public AbstractFnSec(SecuritySelector selector) {
		this.selector = selector;
	}

	protected Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return null;
		} else {
			return context.getAuthentication();
		}
	}
	
	protected UserToken getUserToken() {
		Authentication authentication = getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && authentication.getDetails() instanceof UserToken) {
			return (UserToken) authentication.getDetails();
		} else {
			return null;
		}
	}

}
