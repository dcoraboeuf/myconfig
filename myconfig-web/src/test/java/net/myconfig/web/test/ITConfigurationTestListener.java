package net.myconfig.web.test;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ITConfigurationTestListener implements TestExecutionListener {

	@Override
	public void beforeTestClass(TestContext ctx) throws Exception {
		System.setProperty("it.db", "src/test/resources/it/db.sql");
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
