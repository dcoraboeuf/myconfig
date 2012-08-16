package net.myconfig.web.security;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.exception.AbstractTokenException;
import net.myconfig.service.exception.InputException;
import net.myconfig.service.model.UserSummary;
import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController extends AbstractGUIPage {

	private final SecurityService securityService;

	@Autowired
	public SecurityController(UIInterface ui, ErrorHandler errorHandler, SecurityService securityService) {
		super(ui, errorHandler);
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

	@ExceptionHandler(InputException.class)
	public ModelAndView onInputError(Locale locale, InputException ex) {
		ExtendedModelMap model = prepareModelForError(locale, ex, "error");
		// OK
		return new ModelAndView(users(model), model);
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
		List<UserSummary> users = ui.users();
		model.addAttribute("users", users);
		// OK
		return "users";
	}

	@RequestMapping(value = "/gui/user/create", method = RequestMethod.POST)
	public String userCreate(@RequestParam String name, @RequestParam String email) {
		// Creation
		ui.userCreate(name, email);
		// OK
		return "redirect:/gui/users";
	}

	@RequestMapping(value = "/gui/user/delete", method = RequestMethod.POST)
	public String userDelete(@RequestParam String name) {
		// Creation
		ui.userDelete(name);
		// OK
		return "redirect:/gui/users";
	}

	@RequestMapping(value = "/gui/user/confirm/{name}/{token}", method = RequestMethod.GET)
	public String userConfirmForm(@PathVariable String name, @PathVariable String token, Model model) {
		// Confirms the token
		securityService.checkUserConfirm(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userConfirm";
	}

	@RequestMapping(value = "/gui/user/confirm", method = RequestMethod.POST)
	public String userConfirm(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String password, Model model) {
		try {
			securityService.userConfirm(name, token, password);
			model.addAttribute("name", name);
			return "userConfirmOK";
		} catch (AbstractTokenException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userConfirm";
		}
	}

	@RequestMapping(value = "/gui/user/reset", method = RequestMethod.GET)
	public String userResetLink() {
		securityService.userReset();
		// OK
		return "userResetRequestOK";
	}

	@RequestMapping(value = "/gui/user/reset/{name}/{token}", method = RequestMethod.GET)
	public String userResetForm(@PathVariable String name, @PathVariable String token, Model model) {
		// Confirms the token
		securityService.checkUserReset(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userReset";
	}

	@RequestMapping(value = "/gui/user/reset", method = RequestMethod.POST)
	public String userReset(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
		try {
			securityService.userReset(name, token, oldPassword, newPassword);
			model.addAttribute("name", name);
			return "userResetOK";
		} catch (InputException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userReset";
		}
	}

}
