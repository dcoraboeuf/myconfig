package net.myconfig.acc.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ApplicationsPage extends Page {

	public ApplicationsPage(WebDriver driver) {
		super(driver);
	}

	public void createApplication(String name) {
		// Creates the application with its name
		id("crud-create-text-").sendKeys(name);
		id("crud-create-submit-").click();
		// Waits for the name to appear
		driver.findElement(byApplicationName(name));
	}

	public void deleteApplication(String name) {
		// Gets the delete button for this application
		WebElement deleteButton = xpath("//td[contains(@class,'crud-column-name') and contains(text(), '%s')]/parent::tr//input[contains(@class,'crud-delete')]", name);
		// Clicks it
		deleteButton.click();
		// Waits for the confirmation
		Alert alert = driver.switchTo().alert();
		// TODO Confirms the text (code + app name)
		// OK
		alert.accept();
		// Waits for the application list to be reloaded
		id("crud-table-applications");
		// TODO Checks the application is not there any longer
		// WebDriverWait wait = new WebDriverWait(driver, 2);
		// wait.until(ExpectedConditions.presenceOfElementLocated(byApplicationName(name)));
	}

	protected By byApplicationName(String name) {
		return byElement("td", "crud-column-name", name);
	}
	

}
