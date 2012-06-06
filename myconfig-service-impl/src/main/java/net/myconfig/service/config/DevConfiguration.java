package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.DEV)
public class DevConfiguration implements GeneralConfiguration {
	
	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService(
				MyConfigProfiles.DEV,
				"classpath:log4j_dev.properties",
				"org.h2.Driver",
				String.format("jdbc:h2:file:%s/myconfig-dev/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")),
				"sa",
				"",
				1,
				2);
	}

}
