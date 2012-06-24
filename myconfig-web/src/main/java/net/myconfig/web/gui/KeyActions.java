package net.myconfig.web.gui;

import org.springframework.ui.Model;

public interface KeyActions {
	
	String keyCreate (Model model, int application, String name, String description);
	
	String keyDelete (Model model, int application, String name);

}
