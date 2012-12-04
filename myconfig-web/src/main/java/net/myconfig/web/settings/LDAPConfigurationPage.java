package net.myconfig.web.settings;

import net.myconfig.service.api.security.LDAPConfiguration;
import net.myconfig.service.api.security.LDAPConfigurator;
import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LDAPConfigurationPage extends AbstractGUIPage {
	
	private final LDAPConfigurator configurator;

	@Autowired
	public LDAPConfigurationPage(UIInterface ui, ErrorHandler errorHandler, LDAPConfigurator configurator) {
		super(ui, errorHandler);
		this.configurator = configurator;
	}
	
	@RequestMapping(value = "/gui/ldap", method = RequestMethod.GET)
	public String settings(Model model) {
		// Loads the LDAP configuration
		model.addAttribute("ldap", configurator.loadConfiguration());
		// OK
		return "ldap";
	}
	
	@RequestMapping(value = "/gui/ldap", method = RequestMethod.POST)
	public String save(LDAPConfiguration configuration) {
		// Saves the LDAP configuration
		configurator.saveConfiguration(configuration);
		// OK - goes back to the settings
		return "redirect:/gui/settings";
	}

}
