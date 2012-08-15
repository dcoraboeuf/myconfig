package net.myconfig.service.impl;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.UIService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.TEST)
public class MockUIService implements UIService {

	@Override
	public String getLink(Link link, String... components) {
		StringBuilder s = new StringBuilder("http://mock/");
		s.append(link);
		for (String component : components) {
			s.append("/").append(component);
		}
		return s.toString();
	}

}
