package net.myconfig.web.test;

import net.myconfig.core.MyConfigProfiles;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(MyConfigProfiles.IT)
public class ITConfigurationTest extends AbstractConfigurationTest {
	
	@Test
	public void ok() {
		// Initialization of the Spring configuration was OK for IT
	}

}
