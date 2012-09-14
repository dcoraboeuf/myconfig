package net.myconfig.web.settings;

import java.util.List;
import java.util.Locale;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserProfile;
import net.myconfig.service.exception.InputException;
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
public class SettingsPage extends AbstractGUIPage {

	private final SecurityService securityService;
	private final SecuritySelector securitySelector;
	private final ConfigurationService configurationService;

	@Autowired
	public SettingsPage(UIInterface ui, ErrorHandler errorHandler, SecurityService securityService, SecuritySelector securitySelector, ConfigurationService configurationService) {
		super(ui, errorHandler);
		this.securityService = securityService;
		this.securitySelector = securitySelector;
		this.configurationService = configurationService;
	}

	@ExceptionHandler(InputException.class)
	public ModelAndView onInputError(Locale locale, InputException ex) {
		ExtendedModelMap model = prepareModelForError(locale, ex, "error");
		// OK
		return new ModelAndView(settings(model), model);
	}

	@RequestMapping(value = "/gui/settings", method = RequestMethod.GET)
	public String settings(Model model) {
		
		// Application settings
		model.addAttribute("appName", configurationService.getParameter(ConfigurationKey.APP_NAME));
		model.addAttribute("appReplytoAddress", configurationService.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS));
		model.addAttribute("appReplytoName", configurationService.getParameter(ConfigurationKey.APP_REPLYTO_NAME));

		// Security settings
		// Selected security mode
		model.addAttribute("selectedSecurityMode", securitySelector.getSecurityMode());
		// List of modes
		List<String> securityModes = securitySelector.getSecurityModes();
		model.addAttribute("securityModes", securityModes);

		// User settings
		// User data
		UserProfile profile = securitySelector.getCurrentProfile();
		if (securitySelector.allowLogin() && profile != null) {
			model.addAttribute("userDisplayName", profile.getDisplayName());
			model.addAttribute("userEmail", profile.getEmail());
		}

		// OK
		return "settings";
	}

	@RequestMapping(value = "/gui/settings/security/mode", method = RequestMethod.POST)
	public String setSecurityMode(@RequestParam String mode) {
		// Saves the security mode
		securityService.setSecurityMode(mode);
		// OK
		return "redirect:/";
	}

	@RequestMapping(value = "/gui/settings/app", method = RequestMethod.POST)
	public String setApplicationSettings (@RequestParam String name, @RequestParam String replytoAddress, @RequestParam String replytoName) {
		configurationService.setParameter(ConfigurationKey.APP_NAME, name);
		configurationService.setParameter(ConfigurationKey.APP_REPLYTO_ADDRESS, replytoAddress);
		configurationService.setParameter(ConfigurationKey.APP_REPLYTO_NAME, replytoName);
		// OK
		return "redirect:/";
	}

	@RequestMapping(value = "/gui/settings/user/data", method = RequestMethod.POST)
	public String setUserData (@RequestParam String displayName, @RequestParam String email, @RequestParam String password) {
		// Calls the service
		securityService.updateUserData (password, displayName, email);
		// OK
		return "redirect:/";
	}

}
