package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import net.myconfig.acc.support.AccUtils
import net.myconfig.client.java.MyConfigClient
import net.myconfig.core.model.ConfigurationUpdate
import net.myconfig.core.model.ConfigurationUpdates

import org.junit.BeforeClass
import org.junit.Test

class ITRestConfiguration extends AbstractClientUseCase {
	
	static final String APP = "app1"
	
	static int id
	
	@BeforeClass
	static void initTest() {
		MyConfigClient client = AccUtils.CONTEXT.getClient()
		// Deletes the application if needed
		def summaries = client.applications().getSummaries()
		def summary = summaries.find { it -> (it.getName() == APP) }
		if (summary != null) {
			client.applicationDelete(summary.getId())
		}
		// Test application
		id = client.applicationCreate(APP).getId()
		// Versions
		client.versionCreate(id, "1.0")
		client.versionCreate(id, "1.1")
		client.versionCreate(id, "1.2")
		// Environments
		client.environmentCreate(id, "DEV")
		client.environmentCreate(id, "ACC")
		client.environmentCreate(id, "UAT")
		client.environmentCreate(id, "PROD")
		// Keys
		client.keyCreate(id, "jdbc.user", "User used to connect to the database")
		client.keyCreate(id, "jdbc.password", "Password used to connect to the database")
		// Matrix & configuration
		List<ConfigurationUpdate> updates = new ArrayList<ConfigurationUpdate>()
		["1.0", "1.1", "1.2"].each() {
			version ->
				["jdbc.user", "jdbc.password"].each() {
					key -> client.keyVersionAdd(id, version, key)
					["DEV", "ACC", "UAT", "PROD"].each {
						env -> 
							def value = "$version $env $key"
							updates.add(new ConfigurationUpdate(env, version, key, value))
					}
				}
		}
		client.updateConfiguration(id, new ConfigurationUpdates(updates))
	}
	
	@Test
	void configuration() {
		def configuration = client().applicationConfiguration(id)
		assertNotNull (configuration)
		// Application
		assertEquals (id, configuration.getId())
		assertEquals (APP, configuration.getName())
		// Versions
		def versionList = configuration.getVersionSummaryList()
		assert ["1.0", "1.1", "1.2"] == versionList*.getName()
		assert [2, 2, 2] == versionList*.getKeyCount()
		assert [8, 8, 8] == versionList*.getConfigCount()
		assert [8, 8, 8] == versionList*.getValueCount()
		// Environments
		def envList = configuration.getEnvironmentSummaryList()
		assert ["ACC", "DEV", "PROD", "UAT"] == envList*.getName()
		assert [6, 6, 6, 6] == envList*.getConfigCount()
		assert [6, 6, 6, 6] == envList*.getValueCount()		
		// Keys
		def keyList = configuration.getKeySummaryList()
		assert ["jdbc.password", "jdbc.user"] == keyList*.getName()
		assert ["Password used to connect to the database", "User used to connect to the database"] == keyList*.getDescription()
		assert [3, 3] == keyList*.getVersionCount()
		assert [12, 12] == keyList*.getConfigCount()
		assert [12, 12] == keyList*.getValueCount()
	}
	
	@Test
	void createAndDeleteVersion() {
		// Creates a version
		client().versionCreate(id, "2.0");
		// Checks it has been created
		assert client().applicationConfiguration(id).getVersionSummaryList().find { it.getName() == "2.0" } != null
		// Deletes the version
		client().versionDelete(id, "2.0");
		// Checks it has been deleted
		assert client().applicationConfiguration(id).getVersionSummaryList().find { it.getName() == "2.0" } == null
	}

}
