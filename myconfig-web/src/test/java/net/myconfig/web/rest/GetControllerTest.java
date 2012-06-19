package net.myconfig.web.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.web.renderer.HttpRenderer;
import net.myconfig.web.renderer.HttpRendererService;
import net.myconfig.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class GetControllerTest {

	private static final String VARIANT = "variant";
	private static final String MODE = "mode";
	private static final String APP = "myapp";
	private static final String VERSION = "1.0";
	private static final String ENV = "ENV";
	private static final String KEY = "mykey";
	private static final String VALUE = "myvalue";

	private GetController get;
	private MyConfigService service;
	private HttpRendererService httpRendererService;

	@Before
	public void before() {
		// Strings
		Strings strings = new Strings();
		// Error handler
		ErrorHandler errorHandler = mock(ErrorHandler.class);
		// Service
		service = mock(MyConfigService.class);
		// HTTP Renderer
		httpRendererService = mock(HttpRendererService.class);
		// OK
		get = new GetController(strings, errorHandler, service, httpRendererService);
	}

	@Test
	public void key() {
		when(service.getKey(APP, VERSION, ENV, KEY)).thenReturn(VALUE);
		String value = get.key(APP, VERSION, ENV, KEY);
		assertEquals(VALUE, value);
	}

	@Test
	public void env() throws IOException {
		// Renderer
		@SuppressWarnings("unchecked")
		HttpRenderer<ConfigurationSet> renderer = mock(HttpRenderer.class);
		when(httpRendererService.getRenderer(ConfigurationSet.class, MODE)).thenReturn(renderer);
		// Call
		MockHttpServletResponse response = new MockHttpServletResponse();
		get.env(APP, VERSION, ENV, MODE, VARIANT, response);
		// Checks the render call
		verify(renderer, times(1)).renderer(any(ConfigurationSet.class), eq(VARIANT), same(response));
	}

	@Test
	public void env_default() throws IOException {
		// Renderer
		@SuppressWarnings("unchecked")
		HttpRenderer<ConfigurationSet> renderer = mock(HttpRenderer.class);
		when(httpRendererService.getRenderer(ConfigurationSet.class, MODE)).thenReturn(renderer);
		// Call
		MockHttpServletResponse response = new MockHttpServletResponse();
		get.env_default(APP, VERSION, ENV, MODE, response);
		// Checks the render call
		verify(renderer, times(1)).renderer(any(ConfigurationSet.class), (String) isNull(), same(response));
	}

}
