package net.myconfig.service.impl;

import org.springframework.stereotype.Service;

import net.myconfig.service.api.MyConfigService;

@Service
public class MyConfigServiceImpl implements MyConfigService {

	@Override
	public String getKey(String application, String version, String environment, String key) {
		return String.format("%s|%s|%s|%s", application, version, environment, key);
	}

}
