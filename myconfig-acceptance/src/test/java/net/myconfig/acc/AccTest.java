package net.myconfig.acc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public abstract class AccTest {

	private final SimpleDateFormat uidFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	protected WebDriver driver;

	@Before
	public void setUp() throws Exception {
		String xvfbDisplay = System.getProperty("xvfb.display");
		if (StringUtils.isNotBlank(xvfbDisplay)) {
			System.out.println("Setting the Firefox driver on display " + xvfbDisplay);
			FirefoxBinary firefox = new FirefoxBinary();
			firefox.setEnvironmentProperty("DISPLAY", xvfbDisplay);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			driver = new FirefoxDriver(firefox, firefoxProfile);
		} else {
			driver = new FirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception {
		// Configuration: exiting the driver
		driver.quit();
	}

	protected String generateUniqueName(String prefix) {
		return prefix + generateUniqueId();
	}

	protected String generateUniqueId() {
		return uidFormat.format(new Date());
	}

}
