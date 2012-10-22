package net.myconfig.acc.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import net.myconfig.acc.support.AbstractUseCase;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class AbstractClientUseCase extends AbstractUseCase {

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
	
	protected String uid (String prefix) {
		return String.format("%s_%s", prefix, UUID.randomUUID());
	}
}
