package net.myconfig.web.test;

import net.myconfig.core.MyConfigProfiles;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(MyConfigProfiles.TEST)
public class UTConfigurationTest extends AbstractConfigurationTest {
	
	@Test
	public void ok() {
		// Initialization of the Spring configuration was OK for TEST
	}

}
