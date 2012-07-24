package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.TEST)
public class UnitTestConfiguration extends CommonConfiguration {
	
	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService(
				MyConfigProfiles.TEST,
				"classpath:log4j_dev.properties");
	}

}
