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
		xpath("//td[@class='crud-column-name' and text() = '%s']", name);
	}
	

}
