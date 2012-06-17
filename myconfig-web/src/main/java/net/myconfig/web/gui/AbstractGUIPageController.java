package net.myconfig.web.gui;

import java.util.Locale;

import net.myconfig.service.exception.InputException;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;

public abstract class AbstractGUIPageController extends AbstractGUIController {

	@Autowired
	public AbstractGUIPageController(UIInterface ui, ErrorHandler errorHandler) {
		super(ui, errorHandler);
	}

	protected ExtendedModelMap prepareModelForError(Locale locale, InputException ex) {
		// Model
		ExtendedModelMap model = new ExtendedModelMap();
		// Error handling
		model.addAttribute(getErrorKeyInModel(), errorHandler.displayableError(ex, locale));
		// OK
		return model;
	}

	protected String getErrorKeyInModel() {
		return "error";
	}

}
