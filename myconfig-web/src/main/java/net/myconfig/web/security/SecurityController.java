package net.myconfig.web.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

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

}
