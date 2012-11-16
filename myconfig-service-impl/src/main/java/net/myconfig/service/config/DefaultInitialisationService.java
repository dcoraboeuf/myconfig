package net.myconfig.service.config;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultInitialisationService {

	private final Logger logger = LoggerFactory.getLogger(DefaultInitialisationService.class);
	
	private final String profiles;
	private final String version;
	
	@Autowired
	public DefaultInitialisationService (@Value("${app.version}") String version, ApplicationContext ctx) {
		this.profiles = StringUtils.join(ctx.getEnvironment().getActiveProfiles(), ",");
		this.version = version;
	}
	
	@PostConstruct
	public void init() throws FileNotFoundException {
		initGeneral();
	}

	protected void initGeneral() {
		logger.info("[config] With JDK:      {}", System.getProperty("java.version"));
		logger.info("[config] With profiles: {}", profiles);
		logger.info("[config] With version:  {}", version);
	}

}
