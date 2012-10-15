package net.myconfig.acc.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.myconfig.acc.support.AccUtils;
import net.myconfig.client.java.MyConfigClient;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;
import net.sf.jstring.Strings;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class AbstractClientUseCase {

	protected MyConfigClient client() {
		return AccUtils.CONTEXT.getClient();
	}
	
	protected Strings strings() {
		return AccUtils.CONTEXT.getStrings();
	}

	protected int application_create(String appName) {
		ApplicationSummary summary = client().applicationCreate(appName);
		assertNotNull(summary);
		return summary.getId();
	}

	protected void application_delete(int id) {
		Ack ack = client().applicationDelete(id);
		assertTrue("Cannot delete application " + id, ack.isSuccess());
	}

	protected ApplicationSummary application_summary(final int id) {
		ApplicationSummaries applications = client().applications();
		return Iterables.find(applications.getSummaries(), new Predicate<ApplicationSummary>() {
			@Override
			public boolean apply(ApplicationSummary o) {
				return id == o.getId();
			}
		}, null);
	}
}
