package net.myconfig.acc.cu;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.DefaultPageContext;
import net.myconfig.acc.page.HomePage;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

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
		WebDriver aDriver = createDriver();
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

	protected static WebDriver createDriver() {
		WebDriver aDriver;
		String xvfbDisplay = System.getProperty("xvfb.display");
		if (StringUtils.isNotBlank(xvfbDisplay)) {
			System.out.println("Setting the Firefox driver on display " + xvfbDisplay);
			FirefoxBinary firefox = new FirefoxBinary();
			firefox.setEnvironmentProperty("DISPLAY", xvfbDisplay);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			aDriver = new FirefoxDriver(firefox, firefoxProfile);
		} else {
			aDriver = new FirefoxDriver();
		}
		aDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		return aDriver;
	}

	@Given("^I am on the list of applications$")
	public void applications() throws Throwable {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}

	@Given("^an unique (\\w+) name")
	public void unique_name(String category) {
		String name = category + "_" + UUID.randomUUID();
		names.put(category, name);
	}

	@When("^I create the application$")
	public void application_create() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.createApplication(name);
	}

	@Then("^I should see the application in the list$")
	public void application_name() throws Throwable {
		String name = getUniqueName(APPLICATION);
		applications.checkForApplication(name);
	}

	@Then("^I should see the \"(.*)\" (\\w+) error$")
	public void error(String message, String category) throws Throwable {
		String name = getUniqueName(category);
		applications.checkForError("error", message, name); 
	}

}
