package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/version")
public class GUIVersionController extends AbstractGUIApplicationConfigurationController {
	
	@Autowired
	public GUIVersionController(UIInterface ui, ErrorHandler errorHandler) {
		super (ui, errorHandler);
	}
	
	@Override
	protected String getErrorKeyInModel() {
		return "version_error";
	}

	@RequestMapping(value = "/create/{id:.*}", method = RequestMethod.POST)
	public String versionCreate (@PathVariable int id, String name) {
		ui.versionCreate (id, name);
		return redirectToApplicationConfiguration(id);
	}

	@RequestMapping(value = "/delete/{id:*.}", method = RequestMethod.POST)
	public String versionDelete (@PathVariable int id, String name) {
		ui.versionDelete (id, name);
		return redirectToApplicationConfiguration(id);
	}

}
