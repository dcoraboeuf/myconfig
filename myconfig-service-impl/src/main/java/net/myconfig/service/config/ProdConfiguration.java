package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.InitialisationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(MyConfigProfiles.PROD)
public class ProdConfiguration extends CommonConfiguration {

	@Override
	@Bean
	public InitialisationService initialisationService() throws Exception {
		// No logging file
		return new DefaultInitialisationService(MyConfigProfiles.PROD);
	}

}
