package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.test.AbstractIntegrationTest;

public class MyConfigServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService;
	
	@Test
	public void get_key_ok() {
		String value = myConfigService.getKey("myapp", "1.1", "UAT", "jdbc.user");
		assertEquals ("1.1 jdbc.user UAT", value);
	}

}
