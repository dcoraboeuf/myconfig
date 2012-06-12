package net.myconfig.acc.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Page {

	protected final WebDriver driver;

	public Page(WebDriver driver) {
		this.driver = driver;
	}

	protected WebElement id(String id) {
		return driver.findElement(By.id(id));
	}

	protected WebElement xpath(String xpath, Object... args) {
		return driver.findElement(By.xpath(String.format(
				xpath,
				args)));
	}

}
