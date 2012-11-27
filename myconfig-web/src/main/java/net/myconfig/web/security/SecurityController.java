package net.myconfig.web.security;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.myconfig.core.InputException;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.UserSummaries;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserManager;
import net.myconfig.service.api.security.UserProvider;
import net.myconfig.service.api.security.UserProviderFactory;
import net.myconfig.service.exception.AbstractTokenException;
import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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
	private final UserManager userManager;
	private final UserProviderFactory userProviderFactory;

	@Autowired
	public SecurityController(UIInterface ui, ErrorHandler errorHandler, SecurityService securityService, UserManager userManager, UserProviderFactory userProviderFactory) {
		super(ui, errorHandler);
		this.securityService = securityService;
		this.userManager = userManager;
		this.userProviderFactory = userProviderFactory;
	}

	@RequestMapping("/login")
	public String login(Model model, @RequestParam(required=false, value="url") String url) {
		if (StringUtils.isNotBlank(url)) {
			model.addAttribute("url", url);
		}
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
		// List of providers
		List<UserProvider> providers = userProviderFactory.getEnabledProviders();
		model.addAttribute("providers", providers);
		// List of users
		UserSummaries users = ui.users();
		model.addAttribute("users", users.getSummaries());
		// OK
		return "users";
	}

	@RequestMapping(value = "/gui/user/create", method = RequestMethod.POST)
	public String userCreate(@RequestParam String mode, @RequestParam String name, @RequestParam String displayName, @RequestParam String email) {
		// Creation
		ui.userCreate(mode, name, displayName, email);
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
		userManager.checkUserConfirm(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userConfirm";
	}

	@RequestMapping(value = "/gui/user/forgotten/{name}/{token}", method = RequestMethod.GET)
	public String userForgottenForm(@PathVariable String name, @PathVariable String token, Model model) {
		// Confirms the token
		userManager.checkUserForgotten(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userForgottenForm";
	}

	@RequestMapping(value = "/gui/user/forgotten/set", method = RequestMethod.POST)
	public String userForgottenFormSet(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String password, Model model) {
		try {
			userManager.userForgottenSet(name, token, password);
			model.addAttribute("name", name);
			return "userForgottenFormOK";
		} catch (AbstractTokenException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userForgottenForm";
		}
	}

	@RequestMapping(value = "/gui/user/confirm", method = RequestMethod.POST)
	public String userConfirm(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String password, Model model) {
		try {
			ui.userConfirm(name, token, password);
			model.addAttribute("name", name);
			return "userConfirmOK";
		} catch (AbstractTokenException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userConfirm";
		}
	}

	@RequestMapping(value = "/gui/user/password", method = RequestMethod.GET)
	public String userChangePasswordLink() {
		userManager.userChangePassword();
		// OK
		return "userChangePasswordRequestOK";
	}

	@RequestMapping(value = "/gui/user/password/{name}/{token}", method = RequestMethod.GET)
	public String userChangePasswordForm(@PathVariable String name, @PathVariable String token, Model model) {
		// Confirms the token
		userManager.checkUserChangePassword(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userChangePassword";
	}

	@RequestMapping(value = "/gui/user/password", method = RequestMethod.POST)
	public String userChangePassword(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
		try {
			userManager.userChangePassword(name, token, oldPassword, newPassword);
			model.addAttribute("name", name);
			return "userChangePasswordOK";
		} catch (InputException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userChangePassword";
		}
	}

	@RequestMapping(value = "/gui/user/forgotten", method = RequestMethod.GET)
	public String userForgotten() {
		return "userForgotten";
	}

	@RequestMapping(value = "/gui/user/forgotten", method = RequestMethod.POST)
	public String userForgottenPost(@RequestParam String email, Model model) {
		// e-mail
		model.addAttribute("email", email);
		// Asks for reset
		Ack ack = userManager.userForgotten(email);
		// OK
		if (ack.isSuccess()) {
			return "userForgottenOK";
		} else {
			return "userForgottenNOK";
		}
	}
	
	@RequestMapping(value = "/gui/user/{name}/reset", method = RequestMethod.POST)
	public String userReset(@PathVariable String name) {
		Validate.notBlank(name);
		userManager.userReset(name);
		return "redirect:/gui/users";
	}

	@RequestMapping(value = "/gui/user/reset/{name}/{token}", method = RequestMethod.GET)
	public String userResetForm(@PathVariable String name, @PathVariable String token, Model model) {
		// Confirms the token
		userManager.checkUserReset(name, token);
		// Fills the model
		model.addAttribute("name", name).addAttribute("token", token);
		// OK
		return "userResetForm";
	}

	@RequestMapping(value = "/gui/user/reset", method = RequestMethod.POST)
	public String userResetFormSet(Locale locale, @RequestParam String name, @RequestParam String token, @RequestParam String password, Model model) {
		try {
			userManager.userReset(name, token, password);
			model.addAttribute("name", name);
			return "userResetFormOK";
		} catch (AbstractTokenException ex) {
			model.addAttribute("error", errorHandler.displayableError(ex, locale)).addAttribute("name", name).addAttribute("token", token);
			return "userResetForm";
		}
	}
	
	@RequestMapping(value = "/gui/user/{name}/disable", method = RequestMethod.POST)
	public String userDisable(@PathVariable String name) {
		Validate.notBlank(name);
		securityService.userDisable(name);
		return "redirect:/gui/users";
	}
	
	@RequestMapping(value = "/gui/user/{name}/enable", method = RequestMethod.POST)
	public String userEnable(@PathVariable String name) {
		Validate.notBlank(name);
		securityService.userEnable(name);
		return "redirect:/gui/users";
	}

}
