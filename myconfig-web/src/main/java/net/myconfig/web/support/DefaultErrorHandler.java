package net.myconfig.web.support;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.CoreException;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityUtils;
import net.myconfig.service.api.security.User;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class DefaultErrorHandler implements ErrorHandler {

	private final Logger logger = LoggerFactory.getLogger("User");
	
	private final Strings strings;
	private final SecuritySelector securitySelector;
	
	@Autowired
	public DefaultErrorHandler(Strings strings, SecuritySelector securitySelector) {
		this.strings = strings;
		this.securitySelector = securitySelector;
	}
	
	@Override
	public boolean canHandleAccessDenied() {
		Authentication authentication = SecurityUtils.authentication();
		return (securitySelector.allowLogin() && (authentication == null || !authentication.isAuthenticated() || !(authentication.getDetails() instanceof User)));
	}

	@Override
	public ErrorMessage handleError(HttpServletRequest request, Locale locale,
			Exception ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Error message
		String displayMessage;
		String loggedMessage;
		boolean stackTrace;
		if (ex instanceof LocalizableException) {
			loggedMessage = ((LocalizableException)ex).getLocalizedMessage(strings, Locale.ENGLISH);
			stackTrace = false;
			displayMessage = ((LocalizableException)ex).getLocalizedMessage(strings, locale);
		} else {
			loggedMessage = ex.getMessage();
			stackTrace = true;
			// Gets a display message for this exception class
			displayMessage = strings.get(Locale.ENGLISH, ex.getClass().getName(), false);
			if (StringUtils.isBlank(displayMessage)) {
				displayMessage = strings.get(Locale.ENGLISH, "general.error.message");
			}
		}
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		String formattedLoggedMessage = String.format("[%s] %s", uuid, loggedMessage);
		if (stackTrace) {
			logger.error(formattedLoggedMessage, ex);
		} else {
			logger.error(formattedLoggedMessage);
		}
		// OK
		return new ErrorMessage(uuid, displayMessage);
	}
	
	@Override
	public String displayableError(CoreException ex, Locale locale) {
		return ex.getLocalizedMessage(strings, locale);
	}

}
