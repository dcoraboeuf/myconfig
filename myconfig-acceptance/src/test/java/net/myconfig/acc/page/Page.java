package net.myconfig.acc.page;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

public abstract class Page {

	public static final Function<WebElement, String> webElementTextFn = new WebElementTextFn();

	private static class WebElementTextFn implements Function<WebElement, String> {
		@Override
		public String apply(WebElement e) {
			return e.getText();
		}
	}

	protected final WebDriver driver;

	public Page(WebDriver driver) {
		this.driver = driver;
	}

	public void checkForError(String className, String textPattern, Object... textParameters) {
		// Text to look for
		String text = String.format(textPattern, textParameters);
		// Error element
		WebElement error = driver.findElement(By.className(className));
		assertEquals(
				StringUtils.trim(text),
				StringUtils.trim(error.getText())
				);
	}

	protected WebElement id(String id) {
		return driver.findElement(By.id(id));
	}

	protected WebElement xpath(String xpath, Object... args) {
		return driver.findElement(byXpath(xpath, args));
	}

	protected WebElement element(String tag, String className, String text) {
		return driver.findElement(byElement(tag, className, text));
	}

	protected By byXpath(String xpath, Object... args) {
		return By.xpath(String.format(xpath, args));
	}

	protected By byElement(String tag, String className, String text) {
		return byXpath(String.format("//%s[contains(@class, '%s') and contains(text(), '%s')]", tag, className, text));
	}

}
