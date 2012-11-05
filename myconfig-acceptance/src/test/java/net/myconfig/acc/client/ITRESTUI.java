package net.myconfig.acc.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.myconfig.core.model.ApplicationSummary;

import org.junit.Test;

public class ITRESTUI extends AbstractClientUseCase {

	@Test
	public void version() {
		String actualVersion = client().version();
		assertEquals(context().getVersion(), actualVersion);
	}
	
	@Test
	public void application() {
		// Creates a unique application
		String appName = appName();
		String id = application_create (appId(), appName);
		// Gets the application summary
		ApplicationSummary summary = application_summary (id);
		assertEquals (id, summary.getId());
		assertEquals (appName, summary.getName());
		// TODO Basic stats
		// Deletes the application		
		application_delete (id);
		// Checks it has been deleted
		summary = application_summary(id);
		assertNull (summary);
	}

}
