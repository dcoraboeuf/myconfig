package net.myconfig.acc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.SeleneseTestCase;

public class AppCreateDelete extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://localhost:9999/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	@Test
	public void testAppCreateDelete() throws Exception {
		selenium.open("/myconfig/");
		selenium.click("link=here");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=crud-create-text-", "test");
		selenium.click("css=input[type=\"submit\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("xpath=(//input[@type='image'])[1]");
		assertTrue(selenium.getConfirmation().matches("^\\[M-001\\] Do you want to delete the \"test\" application and all its associated configuration[\\s\\S]$"));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
