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
        // TODO driver.quit();
    }

	@Test
	public void appCreateDelete() throws Exception {
		HomePage home = HomePage.start(driver);
		ApplicationsPage applications = home.ui();
		// TODO Generate a unique name
		applications.createApplication ("Test");
	}
	
}
