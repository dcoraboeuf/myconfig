package net.myconfig.web.gui;

import net.myconfig.core.model.EnvironmentConfiguration;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/configuration/environment")
public class GUIEnvironmentConfigurationPage extends AbstractGUIPage {

	@Autowired
	public GUIEnvironmentConfigurationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@RequestMapping(value = "/{application:\\d+}/{environment:.*}", method = RequestMethod.GET)
	public String page(@PathVariable int application, @PathVariable String environment, Model model) {
		// Loads the configuration
		EnvironmentConfiguration environmentConfiguration = ui.environmentConfiguration (application, environment);
		model.addAttribute("environmentConfiguration", environmentConfiguration);
		// OK
		return "environmentConfiguration";
	}

}
