package net.myconfig.web.gui;

import net.myconfig.service.model.KeyVersionConfiguration;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/version/configuration")
public class GUIVersionConfigurationController extends AbstractGUIPageController {

	@Autowired
	public GUIVersionConfigurationController(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	@RequestMapping(value = "/{id:\\d+}")
	public String versionConfiguration (@PathVariable int id, Model model) {
		// Loads the key x version
		KeyVersionConfiguration configuration = ui.keyVersionConfiguration (id);
		model.addAttribute("configuration", configuration);
		// OK
		return "versionConfiguration";
	}

	@RequestMapping(value = "/{id}/key/create", method = RequestMethod.POST)
	public String keyCreate (@PathVariable int id, String name, String description) {
		ui.keyCreate (id, name, description);
		return redirectToVersionConfiguration(id);
	}

	protected String redirectToVersionConfiguration(int id) {
		return redirect ("version/configuration/" + id);
	}

}
