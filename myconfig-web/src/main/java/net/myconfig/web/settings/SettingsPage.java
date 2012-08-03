package net.myconfig.web.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import net.myconfig.web.gui.AbstractGUIPage;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

@Controller
public class SettingsPage extends AbstractGUIPage {

	private final Logger logger = LoggerFactory.getLogger(SettingsPage.class);

	private final Collection<SettingsCollaborator> settingsCollaborators;

	@Autowired
	public SettingsPage(UIInterface ui, ErrorHandler errorHandler, Collection<SettingsCollaborator> settingsCollaborators) {
		super(ui, errorHandler);
		this.settingsCollaborators = settingsCollaborators;
	}

	@RequestMapping(value = "/gui/settings", method = RequestMethod.GET)
	public String settings(Model model) {
		// Collaborators
		model.addAttribute("settingsCollaborators", settingsCollaborators);
		// FIXME Checks if those settings are allowed for the current user
		// Model parameters for each collaborator
		for (SettingsCollaborator settingsCollaborator : settingsCollaborators) {
			settingsCollaborator.initModel(model);
		}
		// OK
		return "settings";
	}

	@RequestMapping(value = "/gui/settings", method = RequestMethod.POST)
	public String save(WebRequest request) {
		Map<String, String[]> parameters = request.getParameterMap();
		// Log
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			logger.debug("[settings] param {} -> {}", entry.getKey(), StringUtils.join(entry.getValue(), ","));
		}
		// Collection of parameters per collaborator
		Map<String,Map<String,String[]>> parametersPerCollaborator = new HashMap<String, Map<String,String[]>>();
		for (SettingsCollaborator settingsCollaborator : settingsCollaborators) {
			String id = settingsCollaborator.getId();
			
			Map<String,String[]> thisParameters = new HashMap<String, String[]>();
			parametersPerCollaborator.put(id, thisParameters);
			
			String prefix = id + ".";
			for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
				String key = entry.getKey();
				if (StringUtils.startsWith(key, prefix)) {
					String name = StringUtils.substringAfter(key, prefix);
					thisParameters.put(name, entry.getValue());
				}
			}
		}
		// Saves the settings per collaborator
		for (SettingsCollaborator settingsCollaborator : settingsCollaborators) {
			String id = settingsCollaborator.getId();
			Map<String, String[]> thisParameters = parametersPerCollaborator.get(id);
			logger.debug("[settings] collaborator {} -> {}", id, thisParameters);
			settingsCollaborator.save (thisParameters);
		}
		// OK
		return "redirect:/";
	}

}
