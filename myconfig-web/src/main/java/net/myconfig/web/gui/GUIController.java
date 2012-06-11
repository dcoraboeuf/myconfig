package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
