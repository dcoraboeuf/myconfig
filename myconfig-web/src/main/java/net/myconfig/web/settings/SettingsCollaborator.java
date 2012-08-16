package net.myconfig.web.settings;

import java.util.Map;

import org.springframework.ui.Model;

public interface SettingsCollaborator {
	
	String getId();

	void initModel(Model model);

	void save(Map<String, String[]> parameters);

	boolean isAllowed();

}
