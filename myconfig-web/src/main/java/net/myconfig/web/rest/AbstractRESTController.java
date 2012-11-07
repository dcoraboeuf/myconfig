package net.myconfig.web.rest;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.CoreException;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.support.ErrorMessage;
import net.sf.jstring.Strings;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class AbstractRESTController {
	
	protected final Strings strings;
	private final MyConfigService myConfigService;
	private final ErrorHandler errorHandler;
	
	public AbstractRESTController(Strings strings, ErrorHandler errorHandler, MyConfigService myConfigService) {
		this.strings = strings;
		this.errorHandler = errorHandler;
		this.myConfigService = myConfigService;
	}

	protected MyConfigService getMyConfigService() {
		return myConfigService;
	}

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public @ResponseBody
	String version () {
		return myConfigService.getVersion();
	}

	@ExceptionHandler(CoreException.class)
	public ResponseEntity<String> onException (HttpServletRequest request, Locale locale, CoreException ex) {
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Returns a message to display to the user
		String message = strings.get(locale, "general.error", error.getMessage(), error.getUuid());
		// Ok
		return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
