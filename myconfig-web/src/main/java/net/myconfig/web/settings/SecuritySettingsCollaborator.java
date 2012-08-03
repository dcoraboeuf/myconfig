package net.myconfig.web.settings;

import java.util.List;
import java.util.Map;

import net.myconfig.service.api.security.SecuritySelector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SecuritySettingsCollaborator extends AbstractSettingsCollaborator {

	private final SecuritySelector securitySelector;

	@Autowired
	public SecuritySettingsCollaborator(SecuritySelector securitySelector) {
		this.securitySelector = securitySelector;
	}

	@Override
	public void initModel(Model model) {
		// Selected security mode
		model.addAttribute("selectedSecurityMode", securitySelector.getSecurityManagementId());
		// List of modes
		List<String> securityModes = securitySelector.getSecurityModes();
		model.addAttribute("securityModes", securityModes);
	}

	@Override
	public void save(Map<String, String[]> parameters) {
		// Mode
		String mode = getParameter (parameters, "mode", true, null);
		securitySelector.switchSecurityMode (mode);
	}

}
