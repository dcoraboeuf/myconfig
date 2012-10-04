package net.myconfig.web.security;

import java.util.Arrays;

import net.myconfig.core.AppFunction;
import net.myconfig.service.api.MyConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the rights of user over applications and environments.
 * 
 */
@Controller
public class UserRightsController {

	private final MyConfigService myConfigService;

	@Autowired
	public UserRightsController(MyConfigService myConfigService) {
		this.myConfigService = myConfigService;
	}

	/**
	 * List the applications the current authenticated user can administrate for
	 * the <code>user</code> given in parameter.
	 */
	@RequestMapping(value = "/gui/user/{user}/applications", method = RequestMethod.GET)
	public String applications(@PathVariable String user, Model model) {
		// User
		model.addAttribute("user", user);
		// List of applications
		model.addAttribute("applications", myConfigService.getUserApplicationRights(user));
		// List of application functions
		model.addAttribute("appFunctions", Arrays.asList(AppFunction.values()));
		// OK
		return "user_applications";
	}

}
