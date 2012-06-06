package net.myconfig.web.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ProdConfigurationTestListener implements TestExecutionListener {

	private final Logger logger = LoggerFactory.getLogger(ProdConfigurationTestListener.class);

	@Override
	public void beforeTestClass(TestContext ctx) throws Exception {
		// Home folder
		File home = new File ("target/home");
		String homePath = home.getAbsolutePath();
		logger.info ("Home folder {}", homePath);
		// Clears the folder
		if (home.exists()) {
			FileUtils.forceDelete(home);
		}
		// Recreates an empty home holder
		FileUtils.forceMkdir(home);
		// Sets the environment
		logger.info ("Sets the environment");
		System.setProperty("myconfig.home", homePath);
	}

	@Override
	public void afterTestClass(TestContext arg0) throws Exception {
	}

	@Override
	public void afterTestMethod(TestContext arg0) throws Exception {
	}

	@Override
	public void beforeTestMethod(TestContext arg0) throws Exception {
	}

	@Override
	public void prepareTestInstance(TestContext arg0) throws Exception {
	}

}
