package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.service.exception.ApplicationRelatedException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIApplicationConfigurationController extends AbstractGUIPageController {

	@Autowired
	public AbstractGUIApplicationConfigurationController(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@ExceptionHandler
	public ModelAndView onApplicationError(Locale locale, ApplicationRelatedException ex) {
		ExtendedModelMap model = prepareModelForError(locale, ex);
		// OK
		return new ModelAndView(getViewForApplicationError(model, ex), model);
	}

	protected String getViewForApplicationError(ExtendedModelMap model, ApplicationRelatedException ex) {
		return applicationConfigure(ex.getId(), model);
	}

	protected String applicationConfigure(int id, Model model) {
		model.addAttribute("application", ui.applicationConfiguration(id));
		return "configuration";
	}

}
