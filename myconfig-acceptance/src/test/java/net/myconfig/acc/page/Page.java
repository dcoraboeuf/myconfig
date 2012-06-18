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
		return driver.findElement(byXpath(xpath, args));
	}
	
	protected WebElement element (String tag, String className, String text) {
		return driver.findElement(byElement(tag, className, text));
	}

	protected By byXpath(String xpath, Object... args) {
		return By.xpath(String.format(xpath, args));
	}
	
	protected By byElement (String tag, String className, String text) {
		return byXpath(String.format("//%s[contains(@class, '%s') and contains(text(), '%s')]", tag, className, text));
	}

}
