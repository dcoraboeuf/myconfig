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
		createTestApplication(id)
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
		
	@Test
	void env_down() {
		String id = "APPENV3"
		createTestApplication(id)
		// 1
		assert myConfigService.setEnvironmentDown(id, "DEV").isSuccess()
		testEnvs(id, ["ACC", "DEV", "BETA", "PROD"])
		// 2
		assert myConfigService.setEnvironmentDown(id, "DEV").isSuccess()
		testEnvs(id, ["ACC", "BETA", "DEV", "PROD"])
		// 3
		assert myConfigService.setEnvironmentDown(id, "DEV").isSuccess()
		testEnvs(id, ["ACC", "BETA", "PROD", "DEV"])
		// 4 - cannot go down any longer
		assert !myConfigService.setEnvironmentDown(id, "DEV").isSuccess()
		testEnvs(id, ["ACC", "BETA", "PROD", "DEV"])
	}
		
	@Test
	void env_up() {
		String id = "APPENV4"
		createTestApplication(id)
		// 1
		assert myConfigService.setEnvironmentUp(id, "PROD").isSuccess()
		testEnvs(id, ["DEV", "ACC", "PROD", "BETA"])
		// 2
		assert myConfigService.setEnvironmentUp(id, "PROD").isSuccess()
		testEnvs(id, ["DEV", "PROD", "ACC", "BETA"])
		// 3
		assert myConfigService.setEnvironmentUp(id, "PROD").isSuccess()
		testEnvs(id, ["PROD", "DEV", "ACC", "BETA"])
		// 4 - cannot go up any longer
		assert !myConfigService.setEnvironmentUp(id, "PROD").isSuccess()
		testEnvs(id, ["PROD", "DEV", "ACC", "BETA"])
	}

	private createTestApplication(String id) {
		myConfigService.createApplication(id, id)
		myConfigService.createEnvironment(id, "DEV")
		myConfigService.createEnvironment(id, "ACC")
		myConfigService.createEnvironment(id, "BETA")
		myConfigService.createEnvironment(id, "PROD")
		testEnvs(id, ["DEV", "ACC", "BETA", "PROD"])
	}

	private testEnvs(String id, List expectedEnvs) {
		ApplicationConfiguration conf = myConfigService.getApplicationConfiguration(id)
		List<EnvironmentSummary> envs = conf.getEnvironmentSummaryList()
		assert expectedEnvs == envs*.getName()
	}
}
