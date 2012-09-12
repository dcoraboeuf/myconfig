package net.myconfig.web.service;

import static java.lang.String.format;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.service.api.UIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebUIService implements UIService {
	
	private final Logger logger = LoggerFactory.getLogger(UIService.class);

	@Override
	public String getLink(Link link, String... components) {
		// Gets the request from the content
		HttpServletRequest request = UIInterceptor.getCurrentRequest();
		// Logs everything about the request
		log(request);
		// FIXME Gets the root URL
		String root = "http://localhost:8080/myconfig";
		// Building the variable part
		String query;
		switch (link) {
		case NEW_USER:
			query = format("gui/user/confirm/%s/%s", (Object[]) components);
			break;
		case RESET_USER:
			query = format("gui/user/reset/%s/%s", (Object[]) components);
			break;
		default:
			throw new IllegalStateException("UIService.Link is not supported: " + link);
		}
		// OK
		return format("%s/%s", root, query);
	}

	private void log(HttpServletRequest request) {
		logger.debug("[request] Context path: {}", request.getContextPath());
		logger.debug("[request] Server name: {}", request.getServerName());
		logger.debug("[request] Server port: {}", request.getServerPort());
		logger.debug("[request] Request URL: {}", request.getRequestURL());
		logger.debug("[request] Protocol: {}", request.getProtocol());
		logger.debug("[request] Scheme: {}", request.getScheme());
		logger.debug("[request] Servlet path: {}", request.getServletPath());
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			@SuppressWarnings("unchecked")
			Enumeration<String> values = request.getHeaders(name);
			while (values.hasMoreElements()) {
				String value = values.nextElement();
				logger.debug("[request] Header {} = {}", name, value);
			}
		}
	}

}
