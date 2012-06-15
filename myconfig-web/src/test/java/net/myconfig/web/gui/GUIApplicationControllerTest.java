package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIApplicationControllerTest extends AbstractConfigurationTest {
	
	@Autowired
	private GUIApplicationsController controller;
	
	/**
	 * FIXME How to unit test a MVC controller together with its error handling?
	 */
	@Test
	@Ignore
	public void applicationCreateAlreadyExists () {
		String view = controller.applicationCreate("test");
		assertEquals ("applications", view);
		//assertEquals (summaries, model.get("applications"));
		//assertEquals ("[S-002] The application with name \"test\" is already defined.", model.get("error"));
	}

}
