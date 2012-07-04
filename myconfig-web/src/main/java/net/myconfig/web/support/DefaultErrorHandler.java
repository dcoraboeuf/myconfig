package net.myconfig.web.support;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.service.exception.CoreException;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
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
		String displayMessage;
		String loggedMessage;
		if (ex instanceof LocalizableException) {
			loggedMessage = ((LocalizableException)ex).getLocalizedMessage(strings, Locale.ENGLISH);
			displayMessage = ((LocalizableException)ex).getLocalizedMessage(strings, locale);
		} else {
			loggedMessage = ex.getMessage();
			// Gets a display message for this exception class
			displayMessage = strings.get(Locale.ENGLISH, ex.getClass().getName(), false);
			if (StringUtils.isBlank(displayMessage)) {
				displayMessage = strings.get(Locale.ENGLISH, "general.error.message");
			}
		}
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		errors.error(String.format("[%s] %s", uuid, loggedMessage));
		// OK
		return new ErrorMessage(uuid, displayMessage);
	}
	
	@Override
	public String displayableError(CoreException ex, Locale locale) {
		return ex.getLocalizedMessage(strings, locale);
	}

}
