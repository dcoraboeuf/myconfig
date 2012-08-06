package net.myconfig.web.security;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.myconfig.core.UserFunction;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController extends AbstractGUIPage {
	
	@Autowired
	public SecurityController(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
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
	public String userCreate (@RequestParam String name) {
		// Creation
		ui.userCreate(name); 
		// OK
		return "redirect:/gui/users";
	}

}
