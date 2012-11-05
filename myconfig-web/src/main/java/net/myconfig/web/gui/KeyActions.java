package net.myconfig.web.gui;

import org.springframework.ui.Model;

public interface KeyActions {
	
	String keyCreate (Model model, String application, String name, String description);
	
	String keyDelete (Model model, String application, String name);

}
