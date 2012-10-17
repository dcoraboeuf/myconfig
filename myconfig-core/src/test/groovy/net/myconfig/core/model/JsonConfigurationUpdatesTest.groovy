package net.myconfig.core.model

import org.junit.Test

class JsonConfigurationUpdatesTest {
	
	@Test
	void json() {
		// Creates the configuration
		List<ConfigurationUpdate> updates = new ArrayList<ConfigurationUpdate>()
		["1.0", "1.1", "1.2"].each {
			version ->
				["jdbc.user", "jdbc.password"].each {
					key ->
						["DEV", "ACC", "UAT", "PROD"].each {
							env -> 
								def value = "$version $env $key"
								updates.add(new ConfigurationUpdate(env, version, key, value))
						}
				}
		}
		def configurationUpdates = new ConfigurationUpdates(updates)
		// Converts to JSON
		def json = OMF 
	}

}
