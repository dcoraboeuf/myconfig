package net.myconfig.web.gui;

import java.util.Arrays;

import net.myconfig.core.EnvFunction;
import net.myconfig.core.model.EnvironmentUsers;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GUIEnvironmentUsersPage extends AbstractGUIPage {

	@Autowired
	public GUIEnvironmentUsersPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	@RequestMapping(value = "/gui/application/{application}/environment/users/{environment:.*}", method = RequestMethod.GET)
	public String users(@PathVariable String application, @PathVariable String environment, Model model) {
		// Loads the environment users
		EnvironmentUsers users = ui.environmentUsers(application, environment);
		model.addAttribute("context", users);
		// List of environment functions
		model.addAttribute("envFunctions", Arrays.asList(EnvFunction.values()));
		// OK
		return "environment_users";
	}

}
