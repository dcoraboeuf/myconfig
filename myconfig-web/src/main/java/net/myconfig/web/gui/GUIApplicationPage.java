package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/application")
public class GUIApplicationPage extends AbstractGUIApplicationPage {

	@Autowired
	public GUIApplicationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@Override
	@RequestMapping(value = "/{application:\\d+}", method = RequestMethod.GET)
	public String page(@PathVariable int application, Model model) {
		model.addAttribute("application", ui.applicationConfiguration(application));
		return "application";
	}
	
	@Override
	protected String pagePath(int application) {
		return "application/" + application;
	}
	
	@RequestMapping(value = "/{application:\\d+}/key/update", method = RequestMethod.POST)
	public String keyUpdate(Model model, @PathVariable int application, String name, String description) {
		ui.keyUpdate (application, name, description);
		return backToPage (application);
	}

}
