package net.myconfig.web.settings;

import java.util.List;
import java.util.Locale;

import net.myconfig.core.InputException;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
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
	/**
	 * Use a settings service instead
	 * @deprecated
	 */
	@Deprecated
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
		User user = securitySelector.getCurrentProfile();
		if (securitySelector.allowLogin() && user != null) {
			model.addAttribute("userDisplayName", user.getDisplayName());
			model.addAttribute("userEmail", user.getEmail());
		}
		
		// Audit settings
		// FIXME Uses a settings service
		model.addAttribute("auditRetentionDays", configurationService.getParameter(ConfigurationKey.AUDIT_RETENTION_DAYS));

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
	public String setApplicationSettings (@RequestParam String name, @RequestParam String replytoAddress, @RequestParam String replytoName) {
		// FIXME Must be admin! Use a settings service
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
	
	@RequestMapping(value = "/gui/settings/audit/retentionDays", method = RequestMethod.POST)
	public String setAuditRetentionDays(@RequestParam int retentionDays) {
		if (retentionDays < 1) {
			throw new AuditRetentionDaysMustBeDefinedException();
		} else {
			// FIXME Must be admin! Use a settings service
			configurationService.setParameter(ConfigurationKey.AUDIT_RETENTION_DAYS, String.valueOf(retentionDays));
			// OK
			return "redirect:/gui/settings";
		}
	}

}
