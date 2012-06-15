package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.core.CoreException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gui")
public class GUIApplicationsController extends AbstractGUIController {
	
	@Autowired
	public GUIApplicationsController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}

	@ExceptionHandler(CoreException.class)
	public ModelAndView onException (Locale locale, CoreException ex) {
		// Model
		ExtendedModelMap model = new ExtendedModelMap();
		// Error handling
		model.addAttribute("error", errorHandler.displayableError (ex, locale));
		// OK
		return new ModelAndView (applications(model), model);
	}
	
	@RequestMapping("/")
	public String applications (Model model) {
		model.addAttribute("applications", ui.applications());
		return "applications";
	}
	
	@RequestMapping(value = "/application/create", method = RequestMethod.POST)
	public String applicationCreate (String name) {
		ui.applicationCreate (name);
		return redirectToListOfApplications();
	}
	
	@RequestMapping(value = "/application/delete", method = RequestMethod.POST)
	public String applicationCreate (int id) {
		ui.applicationDelete (id);
		return redirectToListOfApplications ();
	}

	@RequestMapping(value = "/application/configure", method = RequestMethod.GET)
	public String applicationConfigure (int id, Model model) {
		model.addAttribute("application", ui.applicationConfiguration(id));
		return "configuration";
	}

}
