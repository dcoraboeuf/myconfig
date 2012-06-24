package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.service.exception.ApplicationRelatedException;
import net.myconfig.service.exception.KeyInputException;
import net.myconfig.service.exception.VersionInputException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGUIApplicationPage extends AbstractGUIPage implements KeyActions, VersionActions {

	public AbstractGUIApplicationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	public abstract String page (int application, Model model);
	
	protected abstract String pagePath (int application);
	
	@ExceptionHandler(KeyInputException.class)
	public ModelAndView onKeyException (Locale locale, KeyInputException ex) {
		return onMgtException(locale, ex, "key_error");
	}
	
	@ExceptionHandler(VersionInputException.class)
	public ModelAndView onVersionException (Locale locale, VersionInputException ex) {
		return onMgtException(locale, ex, "version_error");
	}

	protected ModelAndView onMgtException(Locale locale, ApplicationRelatedException ex, String errorKey) {
		ExtendedModelMap model = prepareModelForError(locale, ex, errorKey);
		return new ModelAndView(page(ex.getId(), model), model);
	}
	
	@Override
	@RequestMapping(value = "/{application:\\d+}/key/create", method = RequestMethod.POST)
	public String keyCreate(Model model, @PathVariable int application, String name, String description) {
		ui.keyCreate (application, name, description);
		return backToPage (application);
	}

	@Override
	@RequestMapping(value = "/{application:\\d+}/key/delete", method = RequestMethod.POST)
	public String keyDelete(Model model, @PathVariable int application, String name) {
		ui.keyDelete(application, name);
		return backToPage (application);
	}
	
	@Override
	@RequestMapping(value = "/{application:\\d+}/version/create", method = RequestMethod.POST)
	public String versionCreate(Model model, @PathVariable int application, String name) {
		ui.versionCreate (application, name);
		return backToPage (application);
	}
	
	@Override
	@RequestMapping(value = "/{application:\\d+}/version/delete", method = RequestMethod.POST)
	public String versionDelete(Model model, @PathVariable int application, String name) {
		ui.versionDelete(application, name);
		return backToPage (application);
	}
	
	protected String backToPage(int application) {
		return redirect(pagePath(application));
	}

}
