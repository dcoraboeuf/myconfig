package net.myconfig.acc.page;

import static org.junit.Assert.*;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class ApplicationsPage extends Page {

	public ApplicationsPage(WebDriver driver) {
		super(driver);
	}

	public void createApplication(String name) {
		doCreateApplication(name);
		// Waits for the name to appear
		driver.findElement(byApplicationName(name));
	}

	public void doCreateApplication(String name) {
		// Creates the application with its name
		id("application-create-name").sendKeys(name);
		id("application-create-submit").click();
	}

	public void deleteApplication(String name) {
		// Gets the delete button for this application
		WebElement deleteButton = xpath("//td[contains(@class,'item-column-name') and contains(text(), '%s')]/parent::tr//input[contains(@class,'item-action-delete')]", name);
		// Clicks it
		deleteButton.click();
		// Waits for the confirmation
		Alert alert = driver.switchTo().alert();
		// Confirms the text (code + app name)
		assertEquals(String.format("[M-001-C] Do you want to delete the \"%s\" application and all its associated configuration?", name), alert.getText());
		// OK
		alert.accept();
		// Waits for the application list to be reloaded
		id("applications");
		// Checks the application is not there any longer
		List<String> appNames = getApplicationNames();
		assertFalse(String.format("%s application has not been deleted", name), appNames.contains(name));
	}

	public List<String> getApplicationNames() {
		List<WebElement> tds = driver.findElements(byXpath("//td[contains(@class, 'item-column-name')]"));
		List<String> appNames = Lists.transform(tds, webElementTextFn);
		return appNames;
	}

	public By byApplicationName(String name) {
		return byElement("td", "item-column-name", name);
	}

}
