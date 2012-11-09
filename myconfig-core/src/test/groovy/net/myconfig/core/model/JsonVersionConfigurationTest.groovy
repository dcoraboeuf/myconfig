package net.myconfig.core.model

import org.junit.Test

import com.netbeetle.jackson.ObjectMapperFactory

class JsonVersionConfigurationTest {
	
	@Test
	void ser_deser() {
		def mapper = ObjectMapperFactory.createObjectMapper()
		// Creates the configuration
		def conf = new VersionConfiguration(
			"myapp",
			"myapp",
			"1.1",
			"1.1",
			"1.2",
			[new Key("key1", "Key 1", "plain", null), new Key("key 2", "Key 2", "regex", "\\d+")],
			[new IndexedValues<String>("DEV", [key1: "DEV key1", key2: "DEV key2"]), new IndexedValues<String>("PROD", [key1: "PROD key1"])])
		// Converts to JSON
		def json = mapper.writeValueAsString(conf)
		// Reads from JSON
		def o = mapper.readValue(json, VersionConfiguration.class)
		// Test
		assert o == conf
	}
	
	@Test
	void deser() {
		def json = /{"id":"myapp","name":"myapp","version":"1.1","previousVersion":"1.1","nextVersion":"1.2","keyList":[{"name":"key1","description":"Key 1","typeId":"plain","typeParam":null},{"name":"key 2","description":"Key 2","typeId":"regex","typeParam":"\\d+"}],"environmentValuesPerKeyList":[{"name":"DEV","values":{"key1":"DEV key1","key2":"DEV key2"}},{"name":"PROD","values":{"key1":"PROD key1"}}]}/
		def mapper = ObjectMapperFactory.createObjectMapper()
		// Creates the expected configuration
		def conf = new VersionConfiguration(
			"myapp",
			"myapp",
			"1.1",
			"1.1",
			"1.2",
			[new Key("key1", "Key 1", "plain", null), new Key("key 2", "Key 2", "regex", "\\d+")],
			[new IndexedValues<String>("DEV", [key1: "DEV key1", key2: "DEV key2"]), new IndexedValues<String>("PROD", [key1: "PROD key1"])])
		// Reads from JSON
		def o = mapper.readValue(json, VersionConfiguration.class)
		// Test
		assert o == conf
	}

}
