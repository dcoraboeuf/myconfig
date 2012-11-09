package net.myconfig.core.model

import net.myconfig.core.type.ConfigurationValidationInput
import net.myconfig.core.type.ConfigurationValueValidationInput

import org.junit.Test

import com.netbeetle.jackson.ObjectMapperFactory

class JsonConfigurationValidationInputTest {
	
	@Test
	void ser_deser() {
		def mapper = ObjectMapperFactory.createObjectMapper()
		// Creates the configuration
		def conf = new ConfigurationValidationInput([
			new ConfigurationValueValidationInput("id1", "key1", "value1"),
			new ConfigurationValueValidationInput("id2", "key1", "value2"),
			new ConfigurationValueValidationInput("id3", "key1", "value3")
			])
		// Converts to JSON
		def json = mapper.writeValueAsString(conf)
		// Reads from JSON
		def o = mapper.readValue(json, ConfigurationValidationInput.class)
		// Test
		assert o == conf
	}
	
	@Test
	void deser() {
		def json = """{"validations":[{"id":"value_key1_DEV_2","key":"key1","value":"mm"},{"id":"value_key2_DEV_2","key":"key2","value":"pp"}]}"""
		def mapper = ObjectMapperFactory.createObjectMapper()
		// Creates the expected configuration
		def conf = new ConfigurationValidationInput([
			new ConfigurationValueValidationInput("value_key1_DEV_2", "key1", "mm"),
			new ConfigurationValueValidationInput("value_key2_DEV_2", "key2", "pp")
			])
		// Reads from JSON
		def o = mapper.readValue(json, ConfigurationValidationInput.class)
		// Test
		assert o == conf
	}

}
