package net.myconfig.acc.test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Before;
import org.junit.Test;

public class ITGeneral extends AbstractUseCase {

	private ApplicationsPage applications;

	@Before
	public void applications() {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}

	/**
	 * Following languages should be available on the home page.
	 */
	@Test
	public void availableLanguages() {
		// Asserts the languages
		Collection<String> actualLanguages = applications.getLanguages();
		assertEquals(asList("EN", "FR"), actualLanguages);
	}

	/**
	 * Given I am on the list of applications When I select "FR" as a language
	 * Then the page title is "Applications configurées"
	 */
	@Test
	public void titleInFrench() {
		applications.selectLanguage("FR");
		assertEquals("Applications configurées", applications.getPageTitle());
	}

	/**
	 * Given I am on the list of applications When I select "EN" as a language
	 * Then the page title is "Configured applications"
	 */
	@Test
	public void titleInEnglish() {
		applications.selectLanguage("EN");
		assertEquals("Configured applications", applications.getPageTitle());
	}

}
