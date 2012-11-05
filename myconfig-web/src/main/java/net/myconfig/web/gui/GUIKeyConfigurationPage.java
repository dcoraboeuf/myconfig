package net.myconfig.web.gui;

import net.myconfig.core.model.KeyConfiguration;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/configuration/key")
public class GUIKeyConfigurationPage extends AbstractGUIPage {

	@Autowired
	public GUIKeyConfigurationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@RequestMapping(value = "/{application}/{key:.*}", method = RequestMethod.GET)
	public String page(@PathVariable String application, @PathVariable String key, Model model) {
		// Loads the configuration
		KeyConfiguration keyConfiguration = ui.keyConfiguration (application, key);
		model.addAttribute("keyConfiguration", keyConfiguration);
		// OK
		return "keyConfiguration";
	}

}
