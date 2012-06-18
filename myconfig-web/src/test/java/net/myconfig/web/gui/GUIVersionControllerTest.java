package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.ModelAndView;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIVersionControllerTest extends AbstractConfigurationTest {
	
	@Autowired
	private GUITestHelper helper;
	
	@Autowired
	private UIInterface ui;
	
	@Test
	public void versionCreate () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateName("versionCreate_"));
		ModelAndView mav = helper.run ("POST", "/gui/version/create/" + app.getId(), "name", "1.0");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/configure?id=" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void versionCreate_already_exists () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateName("versionCreate_"));
		helper.run ("POST", "/gui/version/create/" + app.getId(), "name", "1.0");
		ModelAndView mav = helper.run ("POST", "/gui/version/create/" + app.getId(), "name", "1.0");
		assertNotNull (mav);
		assertEquals ("configuration", mav.getViewName());
		ApplicationConfiguration configuration = (ApplicationConfiguration) mav.getModel().get("application");
		assertNotNull(configuration);
		assertEquals (app.getId(), configuration.getId());
		assertEquals(app.getName(), configuration.getName());
		helper.assertErrorMessage (mav, "version_error", "[S-003] The version \"1.0\" is already defined.");
	}

}
