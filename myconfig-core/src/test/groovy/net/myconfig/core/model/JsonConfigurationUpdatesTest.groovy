package net.myconfig.core.model

import org.junit.Test

import com.netbeetle.jackson.ObjectMapperFactory;

class JsonConfigurationUpdatesTest {
	
	@Test
	void ser_deser() {
		def mapper = ObjectMapperFactory.createObjectMapper()
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
		def json = mapper.writeValueAsString(configurationUpdates)
		// Reads from JSON
		def o = mapper.readValue(json, ConfigurationUpdates.class)
		// Test
		assert o == configurationUpdates
	}
	
	@Test
	void deser() {
		def json = """{"updates":[{"environment":"DEV","version":"1.0","key":"jdbc.user","value":"1.0 DEV jdbc.user"},{"environment":"ACC","version":"1.0","key":"jdbc.user","value":"1.0 ACC jdbc.user"},{"environment":"UAT","version":"1.0","key":"jdbc.user","value":"1.0 UAT jdbc.user"},{"environment":"PROD","version":"1.0","key":"jdbc.user","value":"1.0 PROD jdbc.user"},{"environment":"DEV","version":"1.0","key":"jdbc.password","value":"1.0 DEV jdbc.password"},{"environment":"ACC","version":"1.0","key":"jdbc.password","value":"1.0 ACC jdbc.password"},{"environment":"UAT","version":"1.0","key":"jdbc.password","value":"1.0 UAT jdbc.password"},{"environment":"PROD","version":"1.0","key":"jdbc.password","value":"1.0 PROD jdbc.password"},{"environment":"DEV","version":"1.1","key":"jdbc.user","value":"1.1 DEV jdbc.user"},{"environment":"ACC","version":"1.1","key":"jdbc.user","value":"1.1 ACC jdbc.user"},{"environment":"UAT","version":"1.1","key":"jdbc.user","value":"1.1 UAT jdbc.user"},{"environment":"PROD","version":"1.1","key":"jdbc.user","value":"1.1 PROD jdbc.user"},{"environment":"DEV","version":"1.1","key":"jdbc.password","value":"1.1 DEV jdbc.password"},{"environment":"ACC","version":"1.1","key":"jdbc.password","value":"1.1 ACC jdbc.password"},{"environment":"UAT","version":"1.1","key":"jdbc.password","value":"1.1 UAT jdbc.password"},{"environment":"PROD","version":"1.1","key":"jdbc.password","value":"1.1 PROD jdbc.password"},{"environment":"DEV","version":"1.2","key":"jdbc.user","value":"1.2 DEV jdbc.user"},{"environment":"ACC","version":"1.2","key":"jdbc.user","value":"1.2 ACC jdbc.user"},{"environment":"UAT","version":"1.2","key":"jdbc.user","value":"1.2 UAT jdbc.user"},{"environment":"PROD","version":"1.2","key":"jdbc.user","value":"1.2 PROD jdbc.user"},{"environment":"DEV","version":"1.2","key":"jdbc.password","value":"1.2 DEV jdbc.password"},{"environment":"ACC","version":"1.2","key":"jdbc.password","value":"1.2 ACC jdbc.password"},{"environment":"UAT","version":"1.2","key":"jdbc.password","value":"1.2 UAT jdbc.password"},{"environment":"PROD","version":"1.2","key":"jdbc.password","value":"1.2 PROD jdbc.password"}]}"""
		def mapper = ObjectMapperFactory.createObjectMapper()
		// Creates the expected configuration
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
		// Reads from JSON
		def o = mapper.readValue(json, ConfigurationUpdates.class)
		// Test
		assert o == configurationUpdates
	}

}
