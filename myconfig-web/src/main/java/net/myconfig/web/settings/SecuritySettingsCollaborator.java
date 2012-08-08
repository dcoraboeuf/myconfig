package net.myconfig.web.settings;

import java.util.List;
import java.util.Map;

import net.myconfig.service.api.security.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SecuritySettingsCollaborator extends AbstractSettingsCollaborator {

	private final SecurityService securityService;
	
	@Autowired
	public SecuritySettingsCollaborator(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	public void initModel(Model model) {
		// Selected security mode
		model.addAttribute("selectedSecurityMode", securityService.getSecurityMode());
		// List of modes
		List<String> securityModes = securityService.getSecurityModes();
		model.addAttribute("securityModes", securityModes);
	}

	@Override
	public void save(Map<String, String[]> parameters) {
		// Mode
		String mode = getParameter (parameters, "mode", true, null);
		securityService.setSecurityMode(mode);
	}

}
