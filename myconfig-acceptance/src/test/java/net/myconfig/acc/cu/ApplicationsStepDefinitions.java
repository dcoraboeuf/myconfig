package net.myconfig.acc.cu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.DefaultPageContext;
import net.myconfig.acc.page.HomePage;
import net.myconfig.acc.support.AccUtils;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class ApplicationsStepDefinitions {

	private static final String APPLICATION = "application";

	protected DefaultPageContext pageContext;
	protected Map<String, String> names;

	private ApplicationsPage applications;

	@Before
	public void setUp() throws Exception {
		pageContext = new DefaultPageContext(getClass().getSimpleName());
		names = new HashMap<String, String>();
	}

	protected String getUniqueName(String category) {
		String name = names.get(category);
		assertNotNull(name);
		return name;
	}

	@Given("^I am on the list of applications$")
	public void applications() throws Throwable {
		// Goes to the applications page
		HomePage home = new HomePage(ITCUApplications.getDriver(), pageContext);
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

	@Then("^I should see the following languages$")
	public void general_languages(List<String> languages) throws Throwable {
		// Asserts the languages
		Collection<String> actualLanguages = applications.getLanguages();
		assertEquals(languages, actualLanguages);
	}

	@When("^I select \"(\\w\\w)\" as a language$")
	public void general_language_select(String language) throws Throwable {
		applications.selectLanguage(language);
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

	@Then("^the page title is \"(.*)\"$")
	public void general_page_title(String title) throws Throwable {
		assertEquals(title, applications.getPageTitle());
	}

	@Then("^I should see the \"(.*)\" (\\w+) error$")
	public void error(String message, String category) throws Throwable {
		String name = getUniqueName(category);
		applications.checkForError("error", message, name);
	}

}
