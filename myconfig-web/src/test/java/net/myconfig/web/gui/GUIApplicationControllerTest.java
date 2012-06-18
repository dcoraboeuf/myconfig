package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.web.test.AbstractConfigurationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@ActiveProfiles(MyConfigProfiles.TEST)
public class GUIApplicationControllerTest extends AbstractConfigurationTest {

	@Autowired
	private RequestMappingHandlerAdapter handlerAdapter;
	
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	protected ModelAndView run (HttpServletRequest request, HttpServletResponse response) throws Exception {
		HandlerExecutionChain handler = handlerMapping.getHandler(request);
		assertNotNull("No handler found for request, check you request mapping", handler);

		final Object controller = handler.getHandler();
		// if you want to override any injected attributes do it here

		final HandlerInterceptor[] interceptors = handlerMapping.getHandler(request).getInterceptors();
		for (HandlerInterceptor interceptor : interceptors) {
			boolean carryOn = interceptor.preHandle(request, response, controller);
			if (!carryOn) {
				return null;
			}
		}

		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		return mav;
	}
	
	@Test
	public void applicationCreate_ok () throws Exception {
		final String appName = "applicationCreate_ok_" + UUID.randomUUID();
		// Request and parameters
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/gui/application/create");
		request.addParameter("name", appName);
		// Call
		ModelAndView mav = run (request, new MockHttpServletResponse());
		// Checks
		assertNotNull (mav);
		assertEquals ("redirect:/gui/", mav.getViewName());
	}

}
