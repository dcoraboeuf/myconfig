package net.myconfig.web.settings;

import java.util.List;
import java.util.Locale;

import net.myconfig.core.InputException;
import net.myconfig.service.api.ApplicationSettings;
import net.myconfig.service.api.AuditSettings;
import net.myconfig.service.api.EventService;
import net.myconfig.service.api.SettingsService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
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
	private final SettingsService settingsService;
	private final EventService eventService;

	@Autowired
	public SettingsPage(UIInterface ui, ErrorHandler errorHandler, SecurityService securityService, SecuritySelector securitySelector, SettingsService settingsService, EventService eventService) {
		super(ui, errorHandler);
		this.securityService = securityService;
		this.securitySelector = securitySelector;
		this.settingsService = settingsService;
		this.eventService = eventService;
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
		ApplicationSettings applicationSettings = settingsService.getApplicationSettings();
		model.addAttribute("applicationSettings", applicationSettings);

		// Security settings
		// Selected security mode
		model.addAttribute("selectedSecurityMode", securitySelector.getSecurityMode());
		// List of modes
		List<String> securityModes = securitySelector.getSecurityModes();
		model.addAttribute("securityModes", securityModes);

		// User settings
		// User data
		User user = securitySelector.getCurrentProfile();
		if (securitySelector.allowLogin() && user != null) {
			model.addAttribute("userDisplayName", user.getDisplayName());
			model.addAttribute("userEmail", user.getEmail());
		}

		// Audit settings
		AuditSettings auditSettings = settingsService.getAuditSettings();
		model.addAttribute("auditSettings", auditSettings);

		// OK
		return "settings";
	}

	@RequestMapping(value = "/gui/settings/security/mode", method = RequestMethod.POST)
	public String setSecurityMode(@RequestParam String mode) {
		// Saves the security mode
		ui.setSecurityMode(mode);
		// OK
		return "redirect:/";
	}

	@RequestMapping(value = "/gui/settings/app", method = RequestMethod.POST)
	public String setApplicationSettings(@RequestParam String name, @RequestParam String replytoAddress, @RequestParam String replytoName) {
		settingsService.setApplicationSettings(name, replytoAddress, replytoName);
		// OK
		return "redirect:/";
	}

	@RequestMapping(value = "/gui/settings/user/data", method = RequestMethod.POST)
	public String setUserData(@RequestParam String displayName, @RequestParam String email, @RequestParam String password) {
		// Calls the service
		securityService.updateUserData(password, displayName, email);
		// OK
		return "redirect:/";
	}

	@RequestMapping(value = "/gui/settings/audit/retentionDays", method = RequestMethod.POST)
	public String setAuditRetentionDays(@RequestParam int retentionDays) {
		settingsService.setAuditRetentionDays(retentionDays);
		return "redirect:/gui/settings";
	}

	@RequestMapping(value = "/gui/settings/audit/clearAll", method = RequestMethod.GET)
	public String auditClearAll() {
		eventService.clearAll();
		return "redirect:/gui/settings";
	}

}
