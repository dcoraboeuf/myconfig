package net.myconfig.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("uiInterceptor")
public class UIInterceptor extends HandlerInterceptorAdapter {

	private static final ThreadLocal<HttpServletRequest> CURRENT_REQUEST = new ThreadLocal<HttpServletRequest>();

	public static HttpServletRequest getCurrentRequest() {
		HttpServletRequest request = CURRENT_REQUEST.get();
		if (request == null) {
			throw new IllegalStateException("No current request");
		} else {
			return request;
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		CURRENT_REQUEST.set(request);
		// OK
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		CURRENT_REQUEST.set(null);
	}

}
