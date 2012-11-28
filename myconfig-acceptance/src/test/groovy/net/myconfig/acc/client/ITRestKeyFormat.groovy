package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import net.myconfig.client.*
import net.myconfig.client.java.support.ClientMessageException
import net.myconfig.core.model.ConfigurationUpdate
import net.myconfig.core.model.ConfigurationUpdates

import org.junit.Before
import org.junit.Test

/**
 * Integration test for the type of a key
 */
class ITRestKeyFormat extends AbstractClientUseCase {
	
	String id
	String name
	
	@Before
	void initTest() {
		// Test application
		name = appName()
		id = client().applicationCreate(appId(), name).getId()
		// Versions
		client().versionCreate(id, "1.0")
		// Environments
		client().environmentCreate(id, "DEV")
	}
	
	@Test
	void plain_text_as_null() {
		def key = "text0"
		createKey(key, "Plain text key", null, null)
		testValue(key, "")
		testValue(key, "  ")
		testValue(key, "xxx")
	}
	
	@Test
	void plain_text() {
		def key = "text1"
		createKey(key, "Plain text key", "plain", null)
		testValue(key, "")
		testValue(key, "  ")
		testValue(key, "xxx")
	}
	
	@Test(expected = ClientMessageException.class)
	void plain_text_with_arg() {
		def key = "text2"
		createKey(key, "Plain text key", "plain", "xxx")
	}
	
	@Test
	void boolean_format() {
		def key = "boolean1"
		createKey(key, "Boolean key", "boolean", null)
		testValue(key, "true")
		testValue(key, "false")
	}
	
	@Test(expected = ClientMessageException.class)
	void boolean_with_arg() {
		def key = "boolean2"
		createKey(key, "Boolean key", "boolean", "xxx")
	}
	
	@Test(expected = ClientMessageException.class)
	void boolean_wrong_format() {
		def key = "boolean3"
		createKey(key, "Boolean key", "boolean", null)
		testValue(key, "xxx")
	}
	
	protected testValue (String key, String value) {
		client().updateConfiguration(id, new ConfigurationUpdates([
				new ConfigurationUpdate("DEV", "1.0", key, value)
			]))
		assert value == client().key(id, "1.0", "DEV", key)
	}

	protected createKey(String key, String description, String type, String format) {
		client().keyCreate(id, key, description, type, format)
		client().keyVersionAdd(id, "1.0", key)
	}

}
