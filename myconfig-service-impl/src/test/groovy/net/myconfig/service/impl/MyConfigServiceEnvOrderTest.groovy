package net.myconfig.service.impl

import net.myconfig.core.model.ApplicationConfiguration
import net.myconfig.core.model.EnvironmentSummary;
import net.myconfig.core.model.KeySummary
import net.myconfig.service.api.MyConfigService
import net.myconfig.service.db.SQLColumns;
import net.myconfig.test.AbstractIntegrationTest

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MyConfigServiceEnvOrderTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService
		
	@Test
	void env_create_order() {
		String id = "APPENV1"
		myConfigService.createApplication(id, id)
		myConfigService.createEnvironment(id, "DEV")
		myConfigService.createEnvironment(id, "ACC")
		myConfigService.createEnvironment(id, "BETA")
		myConfigService.createEnvironment(id, "PROD")
		ApplicationConfiguration conf = myConfigService.getApplicationConfiguration(id)
		List<EnvironmentSummary> envs = conf.getEnvironmentSummaryList()
		assert ["DEV", "ACC", "BETA", "PROD"] == envs*.getName()
	}
		
	@Test
	void env_delete_order() {
		String id = "APPENV2"
		myConfigService.createApplication(id, id)
		myConfigService.createEnvironment(id, "DEV")
		myConfigService.createEnvironment(id, "TEST")
		myConfigService.createEnvironment(id, "ACC")
		myConfigService.createEnvironment(id, "BETA")
		myConfigService.createEnvironment(id, "PROD")
		ApplicationConfiguration conf = myConfigService.getApplicationConfiguration(id)
		List<EnvironmentSummary> envs = conf.getEnvironmentSummaryList()
		assert ["DEV", "TEST", "ACC", "BETA", "PROD"] == envs*.getName()
		// Deletes the last one
		myConfigService.deleteEnvironment(id, "PROD")
		conf = myConfigService.getApplicationConfiguration(id)
		envs = conf.getEnvironmentSummaryList()
		assert ["DEV", "TEST", "ACC", "BETA"] == envs*.getName()
		["DEV", "TEST", "ACC", "BETA"].eachWithIndex { name, i ->
			int order = i + 1
			assertRecordExists("select ordernb from environment where application = '$id' and name = '$name' and ordernb = $order")
		}
		// Deletes the first one
		myConfigService.deleteEnvironment(id, "DEV")
		conf = myConfigService.getApplicationConfiguration(id)
		envs = conf.getEnvironmentSummaryList()
		assert ["TEST", "ACC", "BETA"] == envs*.getName()
		["TEST", "ACC", "BETA"].eachWithIndex { name, i ->
			int order = i + 1
			assertRecordExists("select ordernb from environment where application = '$id' and name = '$name' and ordernb = $order")
		}
	}
}
