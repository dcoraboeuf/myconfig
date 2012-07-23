package net.myconfig.acc;

import net.myconfig.acc.page.DefaultPageContext;
import net.myconfig.acc.page.PageContext;
import net.myconfig.acc.support.AccUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

public abstract class AccTest {

	protected static WebDriver driver;

	@BeforeClass
	public static void setUp() throws Exception {
		WebDriver aDriver = AccUtils.createDriver();
		driver = aDriver;
	}

	@AfterClass
	public static void tearDown() throws Exception {
		// Configuration: exiting the driver
		driver.quit();
	}

	protected PageContext pageContext;

	@Before
	public void context() {
		pageContext = new DefaultPageContext(getClass().getSimpleName());
	}

}
