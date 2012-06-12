package net.myconfig.acc.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {

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
	}

	public ApplicationsPage ui() {
		driver.findElement(By.id("home-ui-button")).click();
		driver.findElement(By.id("crud-table-applications"));
		return new ApplicationsPage(driver);
	}
	

}
