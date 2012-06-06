package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration implements GeneralConfiguration {
	
	// FIXME Gets the configuration from outside
	// FIXME Logging configuration must be taken from outside
	
	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService (
				MyConfigProfiles.PROD,
				"classpath:log4j_prod.properties",
				"org.h2.Driver",
				String.format("jdbc:h2:file:%s/myconfig-prod/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")),
				"sa",
				"",
				1,
				2);
	}

}
