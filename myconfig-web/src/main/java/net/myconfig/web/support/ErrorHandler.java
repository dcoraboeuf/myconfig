package net.myconfig.web.support;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.CoreException;

public interface ErrorHandler {

	ErrorMessage handleError(HttpServletRequest request, Locale locale, Exception ex);

	String displayableError(CoreException ex, Locale locale);

}
