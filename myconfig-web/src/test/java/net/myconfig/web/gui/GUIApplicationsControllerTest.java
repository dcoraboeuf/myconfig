package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.ModelAndView;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIApplicationsControllerTest extends AbstractConfigurationTest {
	
	@Autowired
	private GUITestHelper helper;
	
	@Test
	public void applicationCreate_ok () throws Exception {
		ModelAndView mav = applicationCreate(helper.generateName("applicationCreate_ok_"));
		assertNotNull (mav);
		assertEquals ("redirect:/gui/", mav.getViewName());
	}
	
	@Test
	public void applicationCreate_already_exists () throws Exception {
		String appName = helper.generateName("applicationCreate_already_exists_");
		// ... once
		applicationCreate(appName);
		// ... twice
		ModelAndView mav = applicationCreate(appName);
		// Same page with an error message
		assertNotNull (mav);
		assertEquals ("applications", mav.getViewName());
		helper.assertErrorMessage(mav, "error", "[S-002] The application with name \"%s\" is already defined.", appName);
	}

	protected ModelAndView applicationCreate(String appName) throws Exception {
		return helper.run("POST", "/gui/application/create", "name", appName);
	}

}
