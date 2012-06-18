package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/environment")
public class GUIEnvironmentController extends AbstractGUIApplicationConfigurationController {
	
	@Autowired
	public GUIEnvironmentController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}
	
	@Override
	protected String getErrorKeyInModel() {
		return "environment_error";
	}

	@RequestMapping(value = "/create/{id:.*}", method = RequestMethod.POST)
	public String environmentCreate (@PathVariable int id, String name) {
		ui.environmentCreate (id, name);
		return redirectToApplicationConfiguration(id);
	}

	@RequestMapping(value = "/delete/{id:.*}", method = RequestMethod.POST)
	public String environmentDelete (@PathVariable int id, String name) {
		ui.environmentDelete (id, name);
		return redirectToApplicationConfiguration(id);
	}

}
