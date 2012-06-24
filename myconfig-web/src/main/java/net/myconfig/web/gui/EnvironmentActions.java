package net.myconfig.web.gui;

import org.springframework.ui.Model;

public interface EnvironmentActions {
	
	String environmentCreate (Model model, int application, String name);
	
	String environmentDelete (Model model, int application, String name);

}
