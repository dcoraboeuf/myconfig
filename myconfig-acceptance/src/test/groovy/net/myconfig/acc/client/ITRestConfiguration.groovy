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
import net.myconfig.core.model.Key

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
		// Creates
		client().versionCreate(id, "2.0")
		// Checks it has been created
		assert client().applicationConfiguration(id).getVersionSummaryList().find { it.getName() == "2.0" } != null
		// Deletes
		client().versionDelete(id, "2.0")
		// Checks it has been deleted
		assert client().applicationConfiguration(id).getVersionSummaryList().find { it.getName() == "2.0" } == null
	}
	
	@Test
	void createAndDeleteEnvironment() {
		// Creates
		client().environmentCreate(id, "BETA")
		// Checks it has been created
		assert client().applicationConfiguration(id).getEnvironmentSummaryList().find { it.getName() == "BETA" } != null
		// Deletes
		client().environmentDelete(id, "BETA");
		// Checks it has been deleted
		assert client().applicationConfiguration(id).getEnvironmentSummaryList().find { it.getName() == "BETA" } == null
	}
	
	@Test
	void createUpdateAndDeleteKey() {
		// Creates a version
		client().keyCreate(id, "mykey", "A description")
		// Checks it has been created
		def key = client().applicationConfiguration(id).getKeySummaryList().find { it.getName() == "mykey" }
		assert key != null
		assert "mykey" == key.getName()
		assert "A description" == key.getDescription()
		
		// Updates
		client().keyUpdate(id, "mykey", "Another description")
		key = client().applicationConfiguration(id).getKeySummaryList().find { it.getName() == "mykey" }
		assert key != null
		assert "mykey" == key.getName()
		assert "Another description" == key.getDescription()
		
		// Deletes
		client().keyDelete(id, "mykey")
		// Checks it has been deleted
		assert client().applicationConfiguration(id).getEnvironmentSummaryList().find { it.getName() == "mykey" } == null
	}
	
	@Test
	void matrix() {
		// Gets the matrix
		def matrix = client().keyVersionConfiguration(id)
		["1.0", "1.1", "1.2"].each() {
			version ->
				["jdbc.user", "jdbc.password"].each() {
					key -> 
						assert matrix.isEnabled(version, key)
				}
		}
	}
	
	@Test
	void matrix_remove_and_add() {
		// Another key
		client().keyCreate(id, "key.test", "Test key")
		try {
			// Check before test
			def matrix = client().keyVersionConfiguration(id)
			assert !matrix.isEnabled("1.2", "key.test")
			// Add
			client().keyVersionAdd(id, "1.2", "key.test")
			matrix = client().keyVersionConfiguration(id)
			assert matrix.isEnabled("1.2", "key.test")
			// Remove
			client().keyVersionRemove(id, "1.2", "key.test")
			matrix = client().keyVersionConfiguration(id)
			assert !matrix.isEnabled("1.2", "key.test")
		} finally {
			client().keyDelete(id, "key.test")
		}
	}
	
	@Test
	void versionConfiguration() {
		def conf = client().versionConfiguration(id, "1.1")
		assert id == conf.getId()
		assert APP == conf.getName()
		assert ["1.0", "1.1", "1.2"] == [ conf.getPreviousVersion(), conf.getVersion(), conf.getNextVersion()]
		assert ["jdbc.password", "jdbc.user"] == conf.getKeyList()*.getName()
		["jdbc.password", "jdbc.user"].each {
			key ->
			["DEV", "ACC", "UAT", "PROD"].each {
				env ->
					def values = conf.getEnvironmentValuesPerKeyList().find { it -> it.getName() == env }
					assert values != null
					assert env == values.getName()
					def value = values.getValues()[key]
					assert "1.1 $env $key" == value
			}
		}
	}
	
	@Test
	void environmentConfiguration() {
		def conf = client().environmentConfiguration(id, "UAT")
		assert id == conf.getId()
		assert APP == conf.getName()
		assert ["PROD", "UAT", null] == [ conf.getPreviousEnvironment(), conf.getEnvironment(), conf.getNextEnvironment()]
		assert ["jdbc.password", "jdbc.user"] == conf.getKeyList()*.getName()
		["1.0", "1.1", "1.2"].each {
			version ->
				def values = conf.getVersionValuesPerKeyList().find { it -> it.getName() == version }
				assert values != null
				assert version == values.getName()
				["jdbc.password", "jdbc.user"].each {
					key ->
						def value = values.getValues()[key]
						assert value.isEnabled()
						assert "$version UAT $key" == value.getValue()
				}
		}
	}
	
	@Test
	void keyConfiguration() {
		def conf = client().keyConfiguration(id, "jdbc.user")
		assert id == conf.getId()
		assert APP == conf.getName()
		assert ["jdbc.password", null] == [ conf.getPreviousKey(), conf.getNextKey()]
		assert new Key("jdbc.user", "User used to connect to the database") == conf.getKey()
		assert ["1.0", "1.1", "1.2"] == conf.getVersionList()*.getName()
		["DEV", "ACC", "UAT", "PROD"].each {
			env ->
				def values = conf.getEnvironmentValuesPerVersionList().find { it -> it.getName() == env }
				assert env == values.getName()
				["1.0", "1.1", "1.2"].each {
					version ->
						def value = values.getValues()[version]
						assert "$version $env jdbc.user" == value
				}
		}
	}

}
