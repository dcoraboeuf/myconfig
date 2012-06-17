package net.myconfig.web.gui;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/application")
public class GUIApplicationConfigurationController extends AbstractGUIApplicationConfigurationController {

	@Autowired
	public GUIApplicationConfigurationController(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@RequestMapping(value = "/configure", method = RequestMethod.GET)
	public String applicationConfigure(int id, Model model) {
		return super.applicationConfigure(id, model);
	}

}
