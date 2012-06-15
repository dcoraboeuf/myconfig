package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.DefaultErrorHandler;
import net.sf.jstring.Strings;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

public class GUIApplicationControllerTest {
	
	private static DefaultErrorHandler errorHandler;

	@BeforeClass
	public static void errorHandler () throws IOException  {
		Strings strings = new Strings("META-INF.resources.exceptions");
		errorHandler = new DefaultErrorHandler(strings);
	}
	
	@Test
	public void applicationCreateAlreadyExists () {
		List<ApplicationSummary> summaries = Collections.emptyList();
		UIInterface ui = mock(UIInterface.class);
		when(ui.applicationCreate("test")).thenThrow(new ApplicationNameAlreadyDefinedException("test"));
		GUIApplicationsController controller = new GUIApplicationsController(ui, errorHandler);
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.applicationCreate("test", model, Locale.ENGLISH);
		assertEquals ("applications", view);
		assertEquals (summaries, model.get("applications"));
		assertEquals ("[S-002] The application with name \"test\" is already defined.", model.get("error"));
	}

}
