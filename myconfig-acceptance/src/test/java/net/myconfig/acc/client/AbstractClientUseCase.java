package net.myconfig.acc.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.myconfig.acc.support.AbstractUseCase;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class AbstractClientUseCase extends AbstractUseCase {

	protected String application_create(String appId, String appName) {
		ApplicationSummary summary = client().applicationCreate(appId, appName);
		assertNotNull(summary);
		return summary.getId();
	}

	protected void application_delete(String id) {
		Ack ack = client().applicationDelete(id);
		assertTrue("Cannot delete application " + id, ack.isSuccess());
	}

	protected ApplicationSummary application_summary(final String id) {
		ApplicationSummaries applications = client().applications();
		return Iterables.find(applications.getSummaries(), new Predicate<ApplicationSummary>() {
			@Override
			public boolean apply(ApplicationSummary o) {
				return StringUtils.equals(id, o.getId());
			}
		}, null);
	}
}
