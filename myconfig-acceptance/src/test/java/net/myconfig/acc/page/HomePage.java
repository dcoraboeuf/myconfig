package net.myconfig.acc.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {

	private static final String ID_HOME_UI_BUTTON = "home-ui-button";

	public HomePage(WebDriver driver, PageContext pageContext) {
		super(driver, pageContext);
		// FIXME Configuration: home page
		driver.get("http://localhost:9999/myconfig");
		// Waits for the content
		driver.findElement(By.id(ID_HOME_UI_BUTTON));
		// Screenshot
		takeScreenshot ("Home");
	}

	public ApplicationsPage ui() {
		driver.findElement(By.id(ID_HOME_UI_BUTTON)).click();
		return new ApplicationsPage(driver, pageContext);
	}
	

}
