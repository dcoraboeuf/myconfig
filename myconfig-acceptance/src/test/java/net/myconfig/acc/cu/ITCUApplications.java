package net.myconfig.acc.cu;

import net.myconfig.acc.support.AccUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
public class ITCUApplications {
	
	private static WebDriver driver;
	
	@BeforeClass
	public static void beforeClass() {
		WebDriver aDriver = AccUtils.createDriver();
		driver = aDriver;
	}
	
	@AfterClass
	public static void afterClass() {
		// TODO Configuration: exiting the driver
		driver.quit();
	}

	public static WebDriver getDriver() {
		return driver;
	}

}
