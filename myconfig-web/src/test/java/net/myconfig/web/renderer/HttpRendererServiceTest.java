package net.myconfig.web.renderer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.test.AbstractIntegrationTest;

public class HttpRendererServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private HttpRendererService httpRendererService;
	
	@Test
	public void jsonAvailable() {
		HttpRenderer<ConfigurationSet> renderer = httpRendererService.getRenderer(ConfigurationSet.class, "json");
		assertNotNull (renderer);
	}
	
	@Test
	public void xmlAvailable() {
		HttpRenderer<ConfigurationSet> renderer = httpRendererService.getRenderer(ConfigurationSet.class, "xml");
		assertNotNull (renderer);
	}
	
	@Test
	public void propertiesAvailable() {
		HttpRenderer<ConfigurationSet> renderer = httpRendererService.getRenderer(ConfigurationSet.class, "properties");
		assertNotNull (renderer);
	}
	
	@Test(expected = RendererNotFoundException.class)
	public void notAvailable() {
		httpRendererService.getRenderer(Object.class, "xxx");
	}

}
