package net.myconfig.acc.page;

import org.openqa.selenium.WebDriver;

public abstract class Page {

	protected final WebDriver driver;

	public Page(WebDriver driver) {
		this.driver = driver;
	}

}
