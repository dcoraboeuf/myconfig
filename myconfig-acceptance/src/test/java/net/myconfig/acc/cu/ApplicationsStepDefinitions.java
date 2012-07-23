package net.myconfig.acc.cu;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.DefaultPageContext;
import net.myconfig.acc.page.HomePage;
import net.myconfig.acc.support.AccUtils;

import org.openqa.selenium.WebDriver;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class ApplicationsStepDefinitions {

	private static final String APPLICATION = "application";

	protected WebDriver driver;
	protected DefaultPageContext pageContext;
	protected Map<String, String> names;

	private ApplicationsPage applications;

	@Before
	public void setUp() throws Exception {
		WebDriver aDriver = AccUtils.createDriver();
		driver = aDriver;
		pageContext = new DefaultPageContext(getClass().getSimpleName());
		names = new HashMap<String, String>();
	}

	@After
	public void tearDown() throws Exception {
		// Configuration: exiting the driver
		driver.quit();
	}

	protected String getUniqueName(String category) {
		String name = names.get(category);
		assertNotNull(name);
		return name;
	}

	@Given("^I am on the list of applications$")
	public void applications() throws Throwable {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}

	@Given("^an unique (\\w+) name")
	public void unique_name(String category) {
		String name = AccUtils.generateUniqueName(category + "_");
		names.put(category, name);
	}

	@When("^I create the application$")
	public void application_create() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.createApplication(name);
	}
	
	@When("^I delete the application$")
	public void application_delete() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.deleteApplication(name);
	}

	@Then("^I should see the application in the list$")
	public void application_name() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.checkForApplication(name);
	}
	
	@Then("^I should not see the application in the list any longer$")
	public void not_application_name() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.checkForApplicationNotPresent(name);
	}

	@Then("^I should see the \"(.*)\" (\\w+) error$")
	public void error(String message, String category) throws Throwable {
		String name = getUniqueName(category);
		applications.checkForError("error", message, name); 
	}

}
