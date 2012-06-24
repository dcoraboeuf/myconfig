package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.support.ErrorMessage;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.ModelAndView;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIApplicationPageTest extends AbstractConfigurationTest {

	@Autowired
	private GUITestHelper helper;
	
	@Autowired
	private UIInterface ui;
	
	@Test
	public void application_configuration() throws Exception {
		ApplicationSummary summary = ui.applicationCreate(helper.generateName("app_config_"));
		ModelAndView mav = helper.run("GET", "/gui/application/"+ summary.getId(), null, null);
		assertNotNull (mav);
		assertEquals ("application", mav.getViewName());
		ApplicationConfiguration app = (ApplicationConfiguration) mav.getModel().get("application");
		assertNotNull (app);
		assertEquals (summary.getId(), app.getId());
		assertEquals (summary.getName(), app.getName());
	}
	
	@Test
	public void application_configuration_not_there() throws Exception {
		ModelAndView mav = helper.run("GET", "/gui/application/0", null, null);
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}

}
