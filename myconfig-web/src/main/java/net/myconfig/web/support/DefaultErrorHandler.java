package net.myconfig.web.support;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.CoreException;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultErrorHandler implements ErrorHandler {

	private final Logger errors = LoggerFactory.getLogger("User");
	
	private final Strings strings;
	
	@Autowired
	public DefaultErrorHandler(Strings strings) {
		this.strings = strings;
	}

	@Override
	public ErrorMessage handleError(HttpServletRequest request, Locale locale,
			Exception ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Error message
		String message;
		if (ex instanceof LocalizableException) {
			message = ((LocalizableException)ex).getLocalizedMessage(strings, Locale.ENGLISH);
		} else {
			// FIXME Better management of generic messages
			message = ex.getMessage();
		}
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		// TODO Custom logging for Prod
		errors.error(String.format("[%s] %s", uuid, message));
		// OK
		return new ErrorMessage(uuid, message);
	}
	
	@Override
	public String displayableError(CoreException ex, Locale locale) {
		return ex.getLocalizedMessage(strings, locale);
	}

}
