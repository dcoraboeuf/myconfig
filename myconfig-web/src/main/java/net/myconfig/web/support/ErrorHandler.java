package net.myconfig.web.support;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public interface ErrorHandler {

	ErrorMessage handleError(HttpServletRequest request, Locale locale, Exception ex);

}
