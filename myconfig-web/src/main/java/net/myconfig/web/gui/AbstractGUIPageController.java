package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.core.CoreException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIPageController extends AbstractGUIController {
	
	@Autowired
	public AbstractGUIPageController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}

	@ExceptionHandler(CoreException.class)
	public ModelAndView onCoreException (Locale locale, CoreException ex) {
		// Model
		ExtendedModelMap model = new ExtendedModelMap();
		// Error handling
		model.addAttribute("error", errorHandler.displayableError (ex, locale));
		// OK
		return new ModelAndView (errorFallbackView(model), model);
	}
	
	protected abstract String errorFallbackView (Model model);

}
