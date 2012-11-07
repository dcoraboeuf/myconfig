package net.myconfig.service.impl

import net.myconfig.core.model.ApplicationConfiguration
import net.myconfig.core.model.KeySummary
import net.myconfig.service.api.MyConfigService
import net.myconfig.test.AbstractIntegrationTest

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MyConfigServiceTypeTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService
		
	@Test
	void keys() {
		ApplicationConfiguration conf = myConfigService.getApplicationConfiguration("APP")
		List<KeySummary> keys = conf.getKeySummaryList()
		assert ["bool", "regex", "text", "text2"] == keys*.getName()
		assert ["boolean", "regex", "plain", "plain"] == keys*.getTypeId()
		assert ["", "\\d+", "", ""] == keys*.getTypeParam()
	}
}
