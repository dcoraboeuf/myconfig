package net.myconfig.web.gui;

import java.util.Arrays;

import net.myconfig.core.AppFunction;
import net.myconfig.core.model.ApplicationUsers;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GUIApplicationUsersPage extends AbstractGUIApplicationPage {

	@Autowired
	public GUIApplicationUsersPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@Override
	@RequestMapping(value = "/gui/application/{application:\\d+}/users", method = RequestMethod.GET)
	public String page(@PathVariable int application, Model model) {
		// Loads the application users
		ApplicationUsers users = ui.applicationUsers(application);
		model.addAttribute("application", users);
		// List of application functions
		model.addAttribute("appFunctions", Arrays.asList(AppFunction.values()));
		// OK
		return "application_users";
	}
	
	@Override
	protected String pagePath(int application) {
		return "application/" + application + "/users";
	}

}
