package net.myconfig.service.config;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import net.myconfig.service.api.ConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Log4jConfigurer;

public class DefaultConfigurationService implements ConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(DefaultConfigurationService.class);

	private final String profile;
	private final String loggingPath;

	public DefaultConfigurationService(String profile, String loggingPath) {
		this.profile = profile;
		this.loggingPath = loggingPath;
	}
	
	@PostConstruct
	public void init() throws FileNotFoundException {
		initGeneral();
		initLogging();
	}

	protected void initLogging() throws FileNotFoundException {
		logger.info("Initializing logging from '{}'", loggingPath);
		Log4jConfigurer.initLogging(loggingPath);
	}

	protected void initGeneral() {
		logger.info("Using {} profile", profile);
	}

}
