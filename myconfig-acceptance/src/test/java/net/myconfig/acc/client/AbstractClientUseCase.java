package net.myconfig.acc.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.myconfig.client.java.MyConfigClient;
import net.myconfig.client.java.support.MyConfigClientFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;

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
}
