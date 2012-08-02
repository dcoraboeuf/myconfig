package net.myconfig.service.config;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import net.myconfig.service.api.InitialisationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInitialisationService implements InitialisationService {

	private final Logger logger = LoggerFactory.getLogger(DefaultInitialisationService.class);

	private final String profile;

	public DefaultInitialisationService(String profile) {
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
