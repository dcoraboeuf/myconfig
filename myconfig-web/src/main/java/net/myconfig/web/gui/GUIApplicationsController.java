package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.core.CoreException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui")
public class GUIApplicationsController extends AbstractGUIController {
	
	@Autowired
	public GUIApplicationsController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}
	
	@RequestMapping("/")
	public String applications (Model model) {
		model.addAttribute("applications", ui.applications());
		return "applications";
	}
	
	@RequestMapping(value = "/application/create", method = RequestMethod.POST)
	public String applicationCreate (String name, Model model, Locale locale) {
		try {
			ui.applicationCreate (name);
			return redirect ("");
		} catch (CoreException ex) {
			return applicationsError(model, locale, ex);
		}
	}

	protected String applicationsError(Model model, Locale locale,
			CoreException ex) {
		// Error handling
		model.addAttribute("error", errorHandler.displayableError (ex, locale));
		// OK
		return applications(model);
	}
	
	@RequestMapping(value = "/application/delete", method = RequestMethod.POST)
	public String applicationCreate (int id, Model model, Locale locale) {
		try {
			ui.applicationDelete (id);
			return redirect ("");
		} catch (CoreException ex) {
			return applicationsError(model, locale, ex);
		}
	}

	@RequestMapping(value = "/application/configure", method = RequestMethod.GET)
	public String applicationConfigure (int id, Model model, Locale locale) {
		try {
			model.addAttribute("application", ui.applicationConfiguration(id));
			return "configuration";
		} catch (CoreException ex) {
			return applicationsError(model, locale, ex);
		}
	}

	@RequestMapping(value = "/version/create/{id}", method = RequestMethod.POST)
	public String versionCreate (@PathVariable int id, String name) {
		// FIXME Error handling
		ui.versionCreate (id, name);
		return configure (id);
	}

	@RequestMapping(value = "/version/delete/{id}", method = RequestMethod.POST)
	public String versionDelete (@PathVariable int id, String name) {
		// FIXME Error handling
		ui.versionDelete (id, name);
		return configure (id);
	}

	protected String configure(int id) {
		return redirect ("application/configure?id=" + id);
	}

	protected String redirect(String path) {
		return "redirect:/gui/" + path;
	}

}
