package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui")
public class GUIApplicationsController extends AbstractGUIPageController {
	
	@Autowired
	public GUIApplicationsController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}
	
	@Override
	protected String errorFallbackView(Model model) {
		return applications(model);
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
