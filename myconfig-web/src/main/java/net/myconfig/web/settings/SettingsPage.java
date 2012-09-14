package net.myconfig.web.settings;

import java.util.List;

import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
		// ... nothing yet...

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

}
