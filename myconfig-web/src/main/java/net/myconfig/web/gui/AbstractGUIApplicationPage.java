package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.service.exception.KeyInputException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIApplicationPage extends AbstractGUIPage implements KeyActions {

	public AbstractGUIApplicationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	public abstract String page (int application, Model model);
	
	@ExceptionHandler(KeyInputException.class)
	public ModelAndView onKeyException (Locale locale, KeyInputException ex) {
		ExtendedModelMap model = prepareModelForError(locale, ex, "key_error");
		// OK
		return new ModelAndView(page(ex.getId(), model), model);
	}
	
	@Override
	@RequestMapping(value = "/{application:\\d+}/key/create", method = RequestMethod.POST)
	public String keyCreate(Model model, @PathVariable int application, String name, String description) {
		ui.keyCreate (application, name, description);
		return page (application, model);
	}
	
	@Override
	@RequestMapping(value = "/{application:\\d+}/key/delete", method = RequestMethod.POST)
	public String keyDelete(Model model, @PathVariable int application, String name) {
		ui.keyDelete(application, name);
		return page (application, model);
	}

}
