package net.myconfig.acc.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {

	private static final String ID_HOME_UI_BUTTON = "home-ui-button";

	public static HomePage start(WebDriver driver) {
		HomePage home = new HomePage(driver);
		home.start();
		return home;
	}

	public HomePage(WebDriver driver) {
		super(driver);
	}

	protected void start() {
		// FIXME Configuration: home page
		driver.get("http://localhost:9999/myconfig");
		// Waits for the content
		driver.findElement(By.id(ID_HOME_UI_BUTTON));
	}

	public ApplicationsPage ui() {
		driver.findElement(By.id(ID_HOME_UI_BUTTON)).click();
		return new ApplicationsPage(driver);
	}
	

}
