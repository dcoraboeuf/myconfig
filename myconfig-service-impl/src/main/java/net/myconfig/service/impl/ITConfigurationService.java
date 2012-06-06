package net.myconfig.service.impl;

import net.myconfig.core.MyConfigProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.IT)
public class ITConfigurationService extends AbstractConfigurationService {
	
	public ITConfigurationService() {
		super (
				MyConfigProfiles.IT,
				"org.h2.Driver",
				"jdbc:h2:file:target/db/data;AUTOCOMMIT=OFF;MVCC=true",
				"sa",
				"",
				1,
				2);
	}

}
