package net.myconfig.web.settings;

import java.util.List;
import java.util.Map;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SecuritySettingsCollaborator extends AbstractSettingsCollaborator {

	private final SecurityService securityService;
	private final SecuritySelector securitySelector;

	@Autowired
	public SecuritySettingsCollaborator(SecurityService securityService, SecuritySelector securitySelector) {
		this.securityService = securityService;
		this.securitySelector = securitySelector;
	}

	@Override
	public void initModel(Model model) {
		// Selected security mode
		model.addAttribute("selectedSecurityMode", securitySelector.getSecurityMode());
		// List of modes
		List<String> securityModes = securitySelector.getSecurityModes();
		model.addAttribute("securityModes", securityModes);
	}

	@Override
	public void save(Map<String, String[]> parameters) {
		// Mode
		String mode = getParameter(parameters, "mode", true, null);
		securityService.setSecurityMode(mode);
	}

	@Override
	public boolean isAllowed() {
		return SecurityUtils.hasOneOfUserFunction(UserFunction.security_setup, UserFunction.security_users);
	}

}
