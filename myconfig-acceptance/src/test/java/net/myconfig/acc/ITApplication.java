package net.myconfig.acc;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Before;
import org.junit.Test;

public class ITApplication extends AccTest {
	
	private ApplicationsPage applications;

	@Before
	public void applications() {
		// Goes to the applications page
		HomePage home = new HomePage(driver);
		applications = home.ui();
	}

	@Test
	public void appCreateDelete() throws Exception {
		// Creates the application
		String appName = generateUniqueName("app");
		applications.createApplication (appName);
		// Deletes the application
		applications.deleteApplication (appName);
	}
	
	@Test
	public void appCreateError() throws Exception {
		// Creates the application
		String appName = generateUniqueName("app");
		applications.createApplication (appName);
		// Creates this application a second time
		applications.doCreateApplication(appName);
		// Checks for the error
		applications.checkForError("error", "[S-002] The application with name \"%s\" is already defined.", appName);
	}
	
}
