package net.myconfig.acc.test;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Before;
import org.junit.Test;

public class ITApplications extends AbstractGUIUseCase {

	private ApplicationsPage applications;

	@Before
	public void applications() {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}

	/**
	 * Given I am on the list of applications And an unique application name
	 * When I create the application Then I should see the application in the
	 * list
	 */
	@Test
	public void createApplication() throws Exception {
		String name = generateUniqueName("app");
		applications.createApplication(name);
		applications.checkForApplication(name);
	}

	/**
	 * Given I am on the list of applications And an unique application name
	 * When I create the application And I create the application Then I should
	 * see the "[S-002] The application with name "%s" is already defined."
	 * application error
	 */
	@Test
	public void createAlreadyExistingApplication() throws Exception {
		String name = generateUniqueName("app");
		applications.createApplication(name);
		// ... a second time
		applications.createApplication(name);
		applications.checkForError("error", "[S-002] The application with name \"%s\" is already defined.", name);
	}

	/**
	 * Creating and deleting an application
	 */
	@Test
	public void createAndDeleteApplication() throws Exception {
		String name = generateUniqueName("app");
		applications.createApplication(name);
		applications.deleteApplication(name);
		applications.checkForApplicationNotPresent(name);
	}

}
