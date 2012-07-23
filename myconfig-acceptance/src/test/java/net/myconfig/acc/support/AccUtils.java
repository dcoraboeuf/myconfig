package net.myconfig.acc.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class AccUtils {

	private static final String UID_FORMAT = "yyyyMMddHHmmssSSS";

	public static WebDriver createDriver() {
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

	public static String generateUniqueName(String prefix) {
		return prefix + generateUniqueId();
	}

	public static String generateUniqueId() {
		return new SimpleDateFormat(UID_FORMAT).format(new Date());
	}

}
