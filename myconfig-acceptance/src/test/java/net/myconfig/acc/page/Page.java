package net.myconfig.acc.page;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class Page {

	private final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

	public static final Function<WebElement, String> webElementTextFn = new WebElementTextFn();

	private static class WebElementTextFn implements Function<WebElement, String> {
		@Override
		public String apply(WebElement e) {
			return e.getText();
		}
	}

	protected final WebDriver driver;
	protected final PageContext pageContext;

	public Page(WebDriver driver, PageContext pageContext) {
		this.driver = driver;
		this.pageContext = pageContext;
	}

	public void takeScreenshot(String name) {
		try {
			takeScreenshot((TakesScreenshot) driver, name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void takeScreenshot(Alert alert, String name) {
		// try {
		// takeScreenshot((TakesScreenshot) alert, name);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
	}

	protected void takeScreenshot(TakesScreenshot takeScreenshot, String name) {
		File file = takeScreenshot.getScreenshotAs(OutputType.FILE);
		// Target file name
		String targetFileName = String.format("%s-%s-%s-%s", pageContext.getName(), timestampFormat.format(new Date()), name, file.getName());
		// Copies to the target file
		pageContext.copyFile(file, "screenshots", targetFileName);
	}

	public void checkForError(String className, String textPattern, Object... textParameters) {
		// Screenshot
		takeScreenshot("Error-Detection");
		// Text to look for
		String text = String.format(textPattern, textParameters);
		// Error element
		WebElement error = driver.findElement(By.className(className));
		assertEquals(StringUtils.trim(text), StringUtils.trim(error.getText()));
	}

	public void closeAlert(String screenshotName, String alertTextPattern, Object... alertTextParameters) {
		String alertText = String.format(alertTextPattern, alertTextParameters);
		// Waits for the confirmation
		Alert alert = driver.switchTo().alert();
		// Confirms the text
		assertEquals(alertText, alert.getText());
		// Screenshot
		takeScreenshot(alert, screenshotName);
		// OK
		alert.accept();
	}

	public Collection<String> getLanguages() {
		List<WebElement> elements = driver.findElements(By.className("language"));
		return Lists.transform(elements, webElementTextFn);
	}

	public void selectLanguage(String language) {
		String id = "language_" + StringUtils.lowerCase(language);
		id(id).click();
		assertEquals("language selected", id(id).getAttribute("class"));
		takeScreenshot("Language-" + language);
	}

	public String getPageTitle() {
		return id("page-title").getText();
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
