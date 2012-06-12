package net.myconfig.acc.page;

import org.openqa.selenium.WebDriver;

public class ApplicationsPage extends Page {

	public ApplicationsPage(WebDriver driver) {
		super(driver);
	}

	public void createApplication(String name) {
		// Creates the application with its name
		id("crud-create-text-").sendKeys(name);
		id("crud-create-submit-").click();
		// Waits for the name to appear
		element ("td", "crud-column-name", name);
	}

	public void deleteApplication(String name) {
		// TODO Gets the delete button for this application
		// xpath("//input[contains(@class,'crud-delete)]", name);
	}
	

}
