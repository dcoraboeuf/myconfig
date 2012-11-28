package net.myconfig.service.impl

import net.myconfig.core.model.ApplicationConfiguration
import net.myconfig.core.model.ConfigurationDescription;
import net.myconfig.core.model.KeySummary
import net.myconfig.service.api.MyConfigService
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.test.AbstractIntegrationTest

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MyConfigServiceGetConfigurationDescriptionTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService
	
	@Test(expected = ApplicationNotFoundException.class)
	void getConfigurationDescription_noapp () {
		myConfigService.getConfigurationDescription("app_xxx", "1.0");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void getConfigurationDescription_noversion () {
		myConfigService.getConfigurationDescription("APP", "1.x");
	}
	
	@Test
	public void getConfigurationDescription_0 () {
		ConfigurationDescription conf = myConfigService.getConfigurationDescription("APP", "1.0")
		assert conf != null
		assert ["DEV", "PROD"] == conf.getEnvironments()*.getName()
		assert ["key1", "key2"] == conf.getKeys()*.getName()
		assert ["Some text 1", "Some boolean 2"] == conf.getKeys()*.getDescription()
		assert ["plain", "boolean"] == conf.getKeys()*.getTypeId()
		assert ["", ""] == conf.getKeys()*.getTypeParam()
	}
	
	@Test
	public void getConfigurationDescription_1 () {
		ConfigurationDescription conf = myConfigService.getConfigurationDescription("APP", "1.1")
		assert conf != null
		assert ["DEV", "PROD"] == conf.getEnvironments()*.getName()
		assert ["key1", "key2", "key3"] == conf.getKeys()*.getName()
		assert ["Some text 1", "Some boolean 2", "Some regex 3"] == conf.getKeys()*.getDescription()
		assert ["plain", "boolean", "regex"] == conf.getKeys()*.getTypeId()
		assert ["", "", "\\d+"] == conf.getKeys()*.getTypeParam()
	}
}
