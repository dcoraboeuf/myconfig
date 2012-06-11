package net.myconfig.web.rest;

import java.util.Locale;
import java.util.UUID;

import net.myconfig.core.CoreException;
import net.myconfig.service.api.MyConfigService;
import net.sf.jstring.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class AbstractRESTController {

	private final Logger errors = LoggerFactory.getLogger("User");
	
	private final Strings strings;
	private final MyConfigService myConfigService;
	
	public AbstractRESTController(Strings strings, MyConfigService myConfigService) {
		this.strings = strings;
		this.myConfigService = myConfigService;
	}
	
	protected Strings getStrings() {
		return strings;
	}

	protected MyConfigService getMyConfigService() {
		return myConfigService;
	}

	@RequestMapping("/version")
	public @ResponseBody
	String version () {
		return myConfigService.getVersion();
	}

	@ExceptionHandler(CoreException.class)
	public ResponseEntity<String> onException (Locale locale, CoreException ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		// TODO Custom logging for Prod
		errors.error(String.format("[%s] %s", uuid, ex.getLocalizedMessage(strings, Locale.ENGLISH)));
		// Returns a message to display to the user
		String message = strings.get(locale, "general.error", ex, uuid);
		// Ok
		return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
