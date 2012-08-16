package net.myconfig.web.settings;

import java.util.Map;

import net.myconfig.service.api.security.SecurityUtils;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class UserSettingsCollaborator extends AbstractSettingsCollaborator {

	@Override
	public void initModel(Model model) {
	}

	@Override
	public void save(Map<String, String[]> parameters) {
	}

	@Override
	public boolean isAllowed() {
		return SecurityUtils.profile() != null;
	}

}
