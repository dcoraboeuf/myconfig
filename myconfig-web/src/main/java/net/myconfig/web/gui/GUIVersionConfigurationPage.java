package net.myconfig.web.gui;

import net.myconfig.core.model.VersionConfiguration;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/configuration/version")
public class GUIVersionConfigurationPage extends AbstractGUIPage {

	@Autowired
	public GUIVersionConfigurationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@RequestMapping(value = "/{application:\\d+}/{version:.*}", method = RequestMethod.GET)
	public String page(@PathVariable int application, @PathVariable String version, Model model) {
		// Loads the configuration
		VersionConfiguration versionConfiguration = ui.versionConfiguration (application, version);
		model.addAttribute("versionConfiguration", versionConfiguration);
		// OK
		return "versionConfiguration";
	}

}
