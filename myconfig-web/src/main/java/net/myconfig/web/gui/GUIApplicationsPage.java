package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.service.exception.InputException;
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
public class GUIApplicationsPage extends AbstractGUIPage {

	private static final String VIEW_APPLICATIONS = "applications";
	private static final String MODEL_APPLICATIONS = VIEW_APPLICATIONS;

	@Autowired
	public GUIApplicationsPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@ExceptionHandler(InputException.class)
	public ModelAndView onApplicationError(Locale locale, InputException ex) {
		ExtendedModelMap model = prepareModelForError(locale, ex, "error");
		// OK
		return new ModelAndView(applications(model), model);
	}
	
	@RequestMapping({"/gui/applications", "/gui/"})
	public String applications (Model model) {
		model.addAttribute(MODEL_APPLICATIONS, ui.applications().getSummaries());
		return VIEW_APPLICATIONS;
	}
	
	@RequestMapping(value = "/gui/applications/create", method = RequestMethod.POST)
	public String applicationCreate (String name) {
		ui.applicationCreate (name);
		return redirect(VIEW_APPLICATIONS);
	}
	
	@RequestMapping(value = "/gui/applications/delete", method = RequestMethod.POST)
	public String applicationDelete (int id) {
		ui.applicationDelete (id);
		return redirect(VIEW_APPLICATIONS);
	}

}
