package net.myconfig.web.gui;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.support.ErrorMessage;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIController {
	
	protected final UIInterface ui;
	protected final ErrorHandler errorHandler;
	
	public AbstractGUIController(UIInterface ui, ErrorHandler errorHandler) {
		this.ui = ui;
		this.errorHandler = errorHandler;
	}

	/**
	 * Generic error handler
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView onException (HttpServletRequest request, Locale locale, Exception ex) {
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Model
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("error", error);
		// OK
		return mav;
	}

}
