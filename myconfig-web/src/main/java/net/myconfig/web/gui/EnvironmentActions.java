package net.myconfig.web.gui;

import org.springframework.ui.Model;

public interface EnvironmentActions {
	
	String environmentCreate (Model model, String application, String name);
	
	String environmentDelete (Model model, String application, String name);

}
