package net.myconfig.web.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.MyConfigProfiles;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
@Profile(MyConfigProfiles.TEST)
public class DefaultGUITestHelper implements GUITestHelper {

	@Autowired
	private RequestMappingHandlerAdapter handlerAdapter;

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	@Autowired
	private ExceptionHandlerExceptionResolver exceptionResolver;
	
	private final AtomicInteger id = new AtomicInteger(0);
	
	@Override
	public String generateId(String prefix) {
		return prefix + id.incrementAndGet();
	}

	@Override
	public String generateName(String prefix) {
		return prefix + UUID.randomUUID();
	}
	
	@Override
	public void assertErrorMessage(ModelAndView mav, String errorKey, String format,
			Object... parameters) {
		String message = (String) mav.getModel().get(errorKey);
		assertEquals (String.format(format, parameters), message);
	}
	
	@Override
	public ModelAndView run(String method, String path, String paramName,
			Object paramValue) throws Exception {
		return run (method, path, paramName != null ? Collections.singletonMap(paramName, paramValue) : null);
	}
	
	@Override
	public ModelAndView run(String method, String path, Map<String,?> params) throws Exception {
		// Request and parameters
		MockHttpServletRequest request = new MockHttpServletRequest(method, path);
		if (params != null) {
			for (Map.Entry<String, ?> entry: params.entrySet()) {
				String paramName = entry.getKey();
				Object paramValue = entry.getValue();
				request.addParameter(paramName, ObjectUtils.toString(paramValue, ""));
			}
		}
		// Call
		return run (request, new MockHttpServletResponse());
	}

	@Override
	public ModelAndView run(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HandlerExecutionChain handler = handlerMapping.getHandler(request);
		assertNotNull(
				"No handler found for request, check you request mapping",
				handler);

		final Object controller = handler.getHandler();

		final HandlerInterceptor[] interceptors = handlerMapping.getHandler(
				request).getInterceptors();
		for (HandlerInterceptor interceptor : interceptors) {
			boolean carryOn = interceptor.preHandle(request, response,
					controller);
			if (!carryOn) {
				return null;
			}
		}

		try {
			return handlerAdapter.handle(request, response, controller);
		} catch (Exception ex) {
			return exceptionResolver.resolveException(request, response,
					controller, ex);
		}
	}

}
