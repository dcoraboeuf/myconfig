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
@RequestMapping("/gui/matrix")
public class GUIMatrixPage extends AbstractGUIApplicationPage {

	@Autowired
	public GUIMatrixPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@Override
	@RequestMapping(value = "/{application:\\d+}", method = RequestMethod.GET)
	public String page(@PathVariable int application, Model model) {
		// Loads the key x version
		KeyVersionConfiguration configuration = ui.keyVersionConfiguration (application);
		model.addAttribute("configuration", configuration);
		// OK
		return "matrix";
	}
	
	@Override
	protected String pagePath(int application) {
		return "matrix/" + application;
	}

}
