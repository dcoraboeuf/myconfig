package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.model.ApplicationSummary;
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
	public void application_list () throws Exception {
		// Initial state
		applicationCreate("app1");
		applicationCreate("app2");
		// Call
		ModelAndView mav = helper.run("GET", "/gui/", null, null);
		// Check
		assertNotNull (mav);
		assertEquals ("applications", mav.getViewName());
		@SuppressWarnings("unchecked")
		List<ApplicationSummary> applications = (List<ApplicationSummary>) mav.getModel().get("applications");
		assertNotNull (applications);
		assertEquals (2, applications.size());
		assertEquals ("app1", applications.get(0).getName());
		assertEquals ("app2", applications.get(1).getName());
	}
	
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
