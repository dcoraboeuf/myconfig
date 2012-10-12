package net.myconfig.acc.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.myconfig.client.java.MyConfigClient;
import net.myconfig.client.java.support.MyConfigClientFactory;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.ApplicationSummaries;
import net.myconfig.core.model.ApplicationSummary;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class AbstractClientUseCase {

	protected static String version;
	protected static MyConfigClient client;

	@BeforeClass
	public static void clientInitialization() throws IOException {
		// FIXME Re-use the configuration code
		String itPort = System.getProperty("it.port");
		if (StringUtils.isBlank(itPort)) {
			itPort = "9999";
		}
		Properties props = new Properties();
		InputStream o = AbstractClientUseCase.class.getResourceAsStream("/Project.properties");
		try {
			props.load(o);
		} finally {
			o.close();
		}
		// Gets the version
		version = props.getProperty("project.version");
		// TODO URL of the server
		String url = String.format("http://localhost:%s/myconfig/ui/", itPort);
		// Creates the client for the API
		client = MyConfigClientFactory.create(url).build();
	}

	protected int application_create(String appName) {
		ApplicationSummary summary = client.applicationCreate(appName);
		assertNotNull(summary);
		return summary.getId();
	}

	protected void application_delete(int id) {
		Ack ack = client.applicationDelete(id);
		assertTrue("Cannot delete application " + id, ack.isSuccess());
	}

	protected ApplicationSummary application_summary(final int id) {
		ApplicationSummaries applications = client.applications();
		return Iterables.find(applications.getSummaries(), new Predicate<ApplicationSummary>() {
			@Override
			public boolean apply(ApplicationSummary o) {
				return id == o.getId();
			}
		});
	}
}
