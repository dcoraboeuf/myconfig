package net.myconfig.web.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.web.renderer.HttpRenderer;
import net.myconfig.web.renderer.HttpRendererService;
import net.myconfig.web.renderer.RendererNotFoundException;
import net.myconfig.web.support.ErrorHandler;
import net.myconfig.web.support.ErrorMessage;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

public class GetControllerTest {

	private static final String VARIANT = "variant";
	private static final String MODE = "mode";
	private static final String APP = "APP";
	private static final String APP_NAME = "myapp";
	private static final String VERSION = "1.0";
	private static final String ENV = "ENV";
	private static final String KEY = "mykey";
	private static final String VALUE = "myvalue";

	private GetController get;
	private MyConfigService service;
	private HttpRendererService httpRendererService;
	private ErrorHandler errorHandler;

	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
		// Strings
		Strings strings = new Strings("META-INF.resources.web-labels");
		// Error handler
		errorHandler = mock(ErrorHandler.class);
		// Service
		service = mock(MyConfigService.class);
		// HTTP Renderer
		httpRendererService = mock(HttpRendererService.class);
		// OK
		get = new GetController(strings, errorHandler, service, httpRendererService);
	}
	
	@Test
	public void onException () {
		// Mock: request
		HttpServletRequest request = mock (HttpServletRequest.class);
		// Mock: error handler
		when(errorHandler.handleError(any(HttpServletRequest.class), any(Locale.class), any(Exception.class))).thenReturn(new ErrorMessage("xxx", "Error message"));
		// Call
		ResponseEntity<String> entity = get.onException(request, Locale.ENGLISH, new ApplicationNotFoundException(APP));
		assertNotNull (entity);
		assertEquals ("An error has occurred.\nMessage: Error message\nReference: xxx", entity.getBody());
		assertEquals (HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
	}
	
	@Test
	public void version() {
		when(service.getVersion()).thenReturn("VX");
		String value = get.version();
		assertEquals ("VX", value);
	}

	@Test
	public void key() {
		when(service.getKey(APP_NAME, VERSION, ENV, KEY)).thenReturn(VALUE);
		String value = get.key(APP_NAME, VERSION, ENV, KEY);
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
		get.env(APP_NAME, VERSION, ENV, MODE, VARIANT, response);
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
		get.env_default(APP_NAME, VERSION, ENV, MODE, response);
		// Checks the render call
		verify(renderer, times(1)).renderer(any(ConfigurationSet.class), (String) isNull(), same(response));
	}
	
	@Test(expected = ConfigurationModeNotFoundException.class)
	public void env_no_mode() throws IOException {
		// Renderer
		when(httpRendererService.getRenderer(ConfigurationSet.class, MODE)).thenThrow(new RendererNotFoundException(ConfigurationSet.class, MODE));
		// Call
		MockHttpServletResponse response = new MockHttpServletResponse();
		get.env(APP_NAME, VERSION, ENV, MODE, VARIANT, response);
	}

}
