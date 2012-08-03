package net.myconfig.web.gui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.settings.SettingsCollaborator;
import net.myconfig.web.support.ErrorHandler;

@Controller
public class SettingsPage extends AbstractGUIPage {

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
		// Model parameters for each collaborator
		for (SettingsCollaborator settingsCollaborator : settingsCollaborators) {
			settingsCollaborator.initModel (model);
		}
		// OK
		return "settings";
	}
	
	@RequestMapping(value = "/gui/settings", method = RequestMethod.POST)
	public String save () {
		// FIXME Saves the settings
		// OK
		return "redirect:/";
	}
	
}
