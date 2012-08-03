package net.myconfig.web.settings;

import org.springframework.ui.Model;

public interface SettingsCollaborator {
	
	String getId();

	void initModel(Model model);

}
