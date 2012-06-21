package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/key")
public class GUIKeyController extends AbstractGUIApplicationConfigurationController {
	
	@Autowired
	public GUIKeyController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}
	
	@Override
	protected String getErrorKeyInModel() {
		return "key_error";
	}

	@RequestMapping(value = "/create/{id:.*}", method = RequestMethod.POST)
	public String keyCreate (@PathVariable int id, String name, String description) {
		ui.keyCreate (id, name, description);
		return redirectToApplicationConfiguration(id);
	}

	@RequestMapping(value = "/delete/{id:.*}", method = RequestMethod.POST)
	public String keyDelete (@PathVariable int id, String name) {
		ui.keyDelete (id, name);
		return redirectToApplicationConfiguration(id);
	}

}
