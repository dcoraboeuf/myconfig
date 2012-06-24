package net.myconfig.web.gui;

import org.springframework.ui.Model;

import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

public abstract class AbstractGUIApplicationPage extends AbstractGUIPage implements KeyActions {

	public AbstractGUIApplicationPage(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}
	
	public abstract String page (int application, Model model);

}
