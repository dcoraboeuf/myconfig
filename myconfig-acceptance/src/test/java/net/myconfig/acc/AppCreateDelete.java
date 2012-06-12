package net.myconfig.acc;

import java.util.concurrent.TimeUnit;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AppCreateDelete {
	
	private WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

	@Test
	public void appCreateDelete() throws Exception {
		HomePage home = HomePage.start(driver);
		ApplicationsPage applications = home.ui();
		
//		selenium.click("link=here");
//		selenium.waitForPageToLoad("30000");
//		selenium.type("id=crud-create-text-", "test");
//		selenium.click("css=input[type=\"submit\"]");
//		selenium.waitForPageToLoad("30000");
//		selenium.click("xpath=(//input[@type='image'])[1]");
//		assertTrue(selenium.getConfirmation().matches("^\\[M-001\\] Do you want to delete the \"test\" application and all its associated configuration[\\s\\S]$"));
	}
	
}
