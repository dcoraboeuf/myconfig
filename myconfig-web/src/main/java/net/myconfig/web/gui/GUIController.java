package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/gui")
public class GUIController {
	
	private final UIInterface ui;

	@Autowired
	public GUIController(UIInterface ui) {
		this.ui = ui;
	}
	
	@RequestMapping("/")
	public String applications (Model model) {
		model.addAttribute("applications", ui.applications());
		return "applications";
	}
	
	@RequestMapping(value = "/application/create", method = RequestMethod.POST)
	public View applicationCreate (String name) {
		ui.applicationCreate (name);
		return redirect ("");
	}
	
	@RequestMapping(value = "/application/delete", method = RequestMethod.POST)
	public View applicationCreate (int id) {
		ui.applicationDelete (id);
		return redirect ("");
	}

	@RequestMapping(value = "/application/configure", method = RequestMethod.GET)
	public String applicationConfigure (int id, Model model) {
		model.addAttribute("application", ui.applicationConfiguration(id));
		return "configuration";
	}

	@RequestMapping(value = "/version/create/{id}", method = RequestMethod.POST)
	public View versionCreate (@PathVariable int id, String name) {
		ui.versionCreate (id, name);
		return configure (id);
	}

	@RequestMapping(value = "/version/delete/{id}", method = RequestMethod.POST)
	public View versionDelete (@PathVariable int id, String name) {
		ui.versionDelete (id, name);
		return configure (id);
	}

	protected View configure(int id) {
		return redirect ("application/configure?id=" + id);
	}

	protected View redirect(String path) {
		return new RedirectView("/gui/" + path, true, false, false);
	}

}
