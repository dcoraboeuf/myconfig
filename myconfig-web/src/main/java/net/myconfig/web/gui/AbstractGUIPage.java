package net.myconfig.web.gui;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.InputException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.support.ErrorMessage;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIPage {

	private static final String ERROR_VIEW = "error";
	private static final String ERROR_KEY = "error";
	
	protected final UIInterface ui;
	protected final ErrorHandler errorHandler; 
	
	public AbstractGUIPage(UIInterface ui, ErrorHandler errorHandler) {
		this.ui = ui;
		this.errorHandler = errorHandler;
	}

	/**
	 * Generic error handler
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView onException (HttpServletRequest request, Locale locale, Exception ex) {
		if (ex instanceof AccessDeniedException) {
			throw (AccessDeniedException) ex;
		}
		// Error message
		ErrorMessage error = errorHandler.handleError (request, locale, ex);
		// Model
		ModelAndView mav = new ModelAndView(ERROR_VIEW);
		mav.addObject(ERROR_KEY, error);
		// OK
		return mav;
	}

	protected ExtendedModelMap prepareModelForError(Locale locale, InputException ex, String errorKey) {
		// Model
		ExtendedModelMap model = new ExtendedModelMap();
		// Error handling
		model.addAttribute(errorKey, errorHandler.displayableError(ex, locale));
		// OK
		return model;
	}

	protected String redirect(String page) {
		return "redirect:/gui/" + page;
	}

}
