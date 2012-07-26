package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.DEV)
public class DevConfiguration extends CommonConfiguration {

	@Override
	@Bean
	public ConfigurationService configurationService() {
		return new DefaultConfigurationService(MyConfigProfiles.DEV);
	}

}
