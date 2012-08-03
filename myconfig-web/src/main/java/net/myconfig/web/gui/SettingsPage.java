package net.myconfig.web.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

@Controller
public class SettingsPage extends AbstractGUIPage {

	@Autowired
	public SettingsPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	@RequestMapping("/gui/settings")
	public String settings(Model model) {
		// OK
		return "settings";
	}

}
