package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.myconfig.core.MyConfigProfiles;
import net.myconfig.core.model.ApplicationConfiguration;
import net.myconfig.core.model.ApplicationSummary;
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
		ApplicationSummary summary = ui.applicationCreate(helper.generateId("APP"), helper.generateName("app_config_"));
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
	
	@Test
	public void version_create () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("versionCreate_"));
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/version/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void version_create_already_exists () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("versionCreate_"));
		helper.run ("POST", "/gui/application/" + app.getId() + "/version/create", "name", "1.0");
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/version/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("application", mav.getViewName());
		ApplicationConfiguration configuration = (ApplicationConfiguration) mav.getModel().get("application");
		assertNotNull(configuration);
		assertEquals (app.getId(), configuration.getId());
		assertEquals(app.getName(), configuration.getName());
		helper.assertErrorMessage (mav, "version_error", "[S-003] The version \"1.0\" is already defined.");
	}
	
	@Test
	public void version_create_noapp () throws Exception {
		ModelAndView mav = helper.run ("POST", "/gui/application/0/version/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}
	
	@Test
	public void version_delete() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("version_delete_"));
		ui.versionCreate(app.getId(), "1.0");
		ui.versionCreate(app.getId(), "1.1");
		ui.versionCreate(app.getId(), "1.2");
		// Deletes one version
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/version/delete", "name", "1.1");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void version_delete_none() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("version_delete_none_"));
		ui.versionCreate(app.getId(), "1.0");
		ui.versionCreate(app.getId(), "1.1");
		// Deletes one version
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/version/delete", "name", "1.2");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void version_delete_noapp() throws Exception {
		ModelAndView mav = helper.run("POST", "/gui/application/0/version/delete", "name", "1.1");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}
	
	@Test
	public void environment_create () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("environmentCreate_"));
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/environment/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void environment_create_already_exists () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("environmentCreate_"));
		helper.run ("POST", "/gui/application/ " + app.getId() + "/environment/create", "name", "1.0");
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/environment/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("application", mav.getViewName());
		ApplicationConfiguration configuration = (ApplicationConfiguration) mav.getModel().get("application");
		assertNotNull(configuration);
		assertEquals (app.getId(), configuration.getId());
		assertEquals(app.getName(), configuration.getName());
		helper.assertErrorMessage (mav, "environment_error", "[S-007] The environment \"1.0\" is already defined.");
	}
	
	@Test
	public void environment_create_noapp () throws Exception {
		ModelAndView mav = helper.run ("POST", "/gui/application/0/environment/create", "name", "1.0");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}
	
	@Test
	public void environment_delete() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("environment_delete_"));
		ui.environmentCreate(app.getId(), "1.0");
		ui.environmentCreate(app.getId(), "1.1");
		ui.environmentCreate(app.getId(), "1.2");
		// Deletes one environment
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/environment/delete", "name", "1.1");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void environment_delete_none() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("environment_delete_none_"));
		ui.environmentCreate(app.getId(), "1.0");
		ui.environmentCreate(app.getId(), "1.1");
		// Deletes one environment
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/environment/delete", "name", "1.2");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void environment_delete_noapp() throws Exception {
		ModelAndView mav = helper.run("POST", "/gui/application/0/environment/delete", "name", "1.1");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}
	
	@Test
	public void key_create () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("keyCreate_"));
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/key/create", "name", "mykey");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void key_create_already_exists () throws Exception {
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("keyCreate_"));
		helper.run ("POST", "/gui/application/" + app.getId() + "/key/create", "name", "mykey");
		ModelAndView mav = helper.run ("POST", "/gui/application/" + app.getId() + "/key/create", "name", "mykey");
		assertNotNull (mav);
		assertEquals ("application", mav.getViewName());
		ApplicationConfiguration configuration = (ApplicationConfiguration) mav.getModel().get("application");
		assertNotNull(configuration);
		assertEquals (app.getId(), configuration.getId());
		assertEquals(app.getName(), configuration.getName());
		helper.assertErrorMessage (mav, "key_error", "[S-008] The key \"mykey\" is already defined.");
	}
	
	@Test
	public void key_create_noapp () throws Exception {
		ModelAndView mav = helper.run ("POST", "/gui/application/0/key/create", "name", "mykey");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}
	
	@Test
	public void key_delete() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("key_delete_"));
		ui.keyCreate(app.getId(), "key1", "Key 1");
		ui.keyCreate(app.getId(), "key2", "Key 2");
		ui.keyCreate(app.getId(), "key3", "Key 3");
		// Deletes one key
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/key/delete", "name", "key2");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void key_delete_none() throws Exception {
		// Test data
		ApplicationSummary app = ui.applicationCreate(helper.generateId("APP"), helper.generateName("key_delete_none_"));
		ui.keyCreate(app.getId(), "key1", "Key 1");
		ui.keyCreate(app.getId(), "key2", "Key 2");
		// Deletes one key
		ModelAndView mav = helper.run("POST", "/gui/application/" + app.getId() + "/key/delete", "name", "key3");
		assertNotNull (mav);
		assertEquals ("redirect:/gui/application/" +  app.getId(), mav.getViewName());
	}
	
	@Test
	public void key_delete_noapp() throws Exception {
		ModelAndView mav = helper.run("POST", "/gui/application/0/key/delete", "name", "key1");
		assertNotNull (mav);
		assertEquals ("error", mav.getViewName());
		ErrorMessage error = (ErrorMessage) mav.getModel().get("error");
		assertEquals ("[S-004] Cannot find application 0", error.getMessage());
	}

}
