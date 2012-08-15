package net.myconfig.web.service;

import static java.lang.String.format;

import org.springframework.stereotype.Service;

import net.myconfig.service.api.UIService;

@Service
public class WebUIService implements UIService {

	@Override
	public String getLink(Link link, String... components) {
		// FIXME Gets the root URL
		String root = "http://localhost:8080/myconfig";
		// Building the variable part
		String query;
		switch (link) {
		case NEW_USER:
			query = format("gui/user/confirm/%s/%s", (Object[]) components);
			break;
		default:
			throw new IllegalStateException("UIService.Link is not supported: " + link);
		}
		// OK
		return format("%s/%s", root, query);
	}

}
