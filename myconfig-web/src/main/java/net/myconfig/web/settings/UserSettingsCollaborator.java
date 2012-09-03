package net.myconfig.web.settings;

import java.util.Map;

import net.myconfig.service.api.security.SecuritySelector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class UserSettingsCollaborator extends AbstractSettingsCollaborator {

	private final SecuritySelector securitySelector;

	@Autowired
	public UserSettingsCollaborator(SecuritySelector securitySelector) {
		this.securitySelector = securitySelector;
	}

	@Override
	public void initModel(Model model) {
	}

	@Override
	public void save(Map<String, String[]> parameters) {
	}

	@Override
	public boolean isAllowed() {
		return securitySelector.isLogged();
	}

}
