package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.PROD)
public class ProdConfigurationService extends AbstractConfigurationService {
	
	// FIXME Gets the configuration from outside
	
	public ProdConfigurationService() {
		super (
				MyConfigProfiles.PROD,
				"org.h2.Driver",
				String.format("jdbc:h2:file:%s/myconfig-prod/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")),
				"sa",
				"",
				1,
				2);
	}

}
