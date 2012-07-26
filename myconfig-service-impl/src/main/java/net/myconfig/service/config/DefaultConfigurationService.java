package net.myconfig.service.config;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import net.myconfig.service.api.ConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigurationService implements ConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(DefaultConfigurationService.class);

	private final String profile;

	public DefaultConfigurationService(String profile) {
		this.profile = profile;
	}
	
	@PostConstruct
	public void init() throws FileNotFoundException {
		initGeneral();
	}

	protected void initGeneral() {
		logger.info("[config] Using {} profile", profile);
	}

}
