package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.web.rest.UIInterface;
import net.myconfig.web.test.AbstractConfigurationTest;
import net.myconfig.web.test.ApplicationSummaryNamePredicate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIApplicationsPageTest extends AbstractConfigurationTest {
	
	@Autowired
	private GUITestHelper helper;

	@Autowired
	private UIInterface ui;
	
	@Test
	public void application_list () throws Exception {
		// Initial number of applications
		int applicationCount = ui.applications().size();
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
		assertEquals (applicationCount + 2, applications.size());
		Iterables.find(applications, new ApplicationSummaryNamePredicate("app1"));
		Iterables.find(applications, new ApplicationSummaryNamePredicate("app2"));
	}
	
	@Test
	public void applicationCreate_ok () throws Exception {
		ModelAndView mav = applicationCreate(helper.generateName("applicationCreate_ok_"));
		assertNotNull (mav);
		assertEquals ("redirect:/gui/applications", mav.getViewName());
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
	
	@Test
	public void application_delete () throws Exception {
		ModelAndView mav = helper.run("POST", "/gui/applications/delete", "id", "0");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/applications", mav.getViewName());
	}

	protected ModelAndView applicationCreate(String appName) throws Exception {
		return helper.run("POST", "/gui/applications/create", "name", appName);
	}

}
