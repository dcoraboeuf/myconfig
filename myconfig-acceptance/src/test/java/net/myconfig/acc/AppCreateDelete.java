package net.myconfig.acc;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Test;

public class AppCreateDelete extends AccTest {

	@Test
	public void appCreateDelete() throws Exception {
		// Goes to the applications page
		HomePage home = HomePage.start(driver);
		ApplicationsPage applications = home.ui();
		// Creates the application
		String appName = generateUniqueName("app");
		applications.createApplication (appName);
		// Deletes the application
		applications.deleteApplication (appName);
	}
	
}
