package net.myconfig.acc.support;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import net.myconfig.acc.client.AbstractClientUseCase;
import net.myconfig.client.java.MyConfigClient;
import net.myconfig.client.java.support.MyConfigClientFactory;
import net.myconfig.core.utils.StringsLoader;
import net.sf.jstring.Strings;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class AccUtils {

	private static final int TIMEOUT = 15;
	private static final String UID_FORMAT = "yyyyMMddHHmmssSSS";
	
	public static final AccContext CONTEXT = getContext();

	private static AccContext getContext() {
		try {
			// Loads the strings
			Strings strings = new StringsLoader().load();
			// Port to use for integration
			String itPort = System.getProperty("it.port");
			if (StringUtils.isBlank(itPort)) {
				itPort = "9999";
			}
			// URL of the server
			// TODO Configuration of the URL
			String url = String.format("http://localhost:%s/myconfig", itPort);
			// Loading general properties
			Properties props = new Properties();
			InputStream o = AbstractClientUseCase.class.getResourceAsStream("/Project.properties");
			try {
				props.load(o);
			} finally {
				o.close();
			}
			// Gets the version
			String version = props.getProperty("project.version");
			// Creates the client for the API
			MyConfigClient client = MyConfigClientFactory.create(url).build();
			// OK
			return new AccContext(url, version, client, strings);
		} catch (IOException ex) {
			throw new RuntimeException("Cannot get acceptance test context", ex);
		}
	}

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
		aDriver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
		return aDriver;
	}

	public static String generateUniqueName(String prefix) {
		return prefix + generateUniqueId();
	}

	public static String generateUniqueId() {
		return new SimpleDateFormat(UID_FORMAT).format(new Date());
	}

}
