package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	protected View redirect(String path) {
		return new RedirectView("/gui/" + path, true, false, false);
	}

}
