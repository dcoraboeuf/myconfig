package net.myconfig.web.gui;

import org.springframework.ui.Model;

public interface VersionActions {
	
	String versionCreate (Model model, String application, String name);
	
	String versionDelete (Model model, String application, String name);

}
