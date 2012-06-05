package net.myconfig.web.test;

import net.myconfig.core.MyConfigProfiles;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(MyConfigProfiles.DEV)
public class DevConfigurationTest extends AbstractConfigurationTest {
	
	@Test
	public void ok() {
		// Initialization of the Spring configuration was OK for DEV
	}

}
