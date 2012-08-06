package net.myconfig.web.security;

import java.util.Arrays;
import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.model.UserSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

	private final SecurityService securityService;

	@Autowired
	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * On logout, goes back to the home page to force re-login
	 */
	@RequestMapping("/logged-out")
	public String loggedOut() {
		return "redirect:/";
	}

	/**
	 * Management of users
	 */
	@RequestMapping("/gui/users")
	public String users(Model model) {
		// List of user functions
		List<UserFunction> userFunctions = Arrays.asList(UserFunction.values());
		model.addAttribute("userFunctions", userFunctions);
		// List of users
		List<UserSummary> users = securityService.getUserList();
		model.addAttribute("users", users);
		// OK
		return "users";
	}

}
