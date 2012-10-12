package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.junit.BeforeClass
import org.junit.Test

class ITRestGet {
	
	static def version
	static def http
	
	@BeforeClass
	static void before() {
		def itPort = System.properties['it.port']
		if (itPort == null || itPort == "") {
			itPort = "9999"
		}
		def props = new Properties()
		getClass().getResourceAsStream("/Project.properties").withStream {
			stream -> props.load(stream)
		}
		version = props["project.version"]
		// URL of the server
		def url = "http://localhost:$itPort/myconfig/get/"
		// Creates the HTTP client for the API
		http = new HTTPBuilder(url)
	}
	
	@Test
	void version() {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "version"
			response.success = { resp, reader ->
				def content = reader.text
				if (version != content) {
					fail("Expected version $version but was $content")
				}
			}
		}
	}
	
	@Test
	void get_key_ok() {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "key/myapp/UAT/1.2/jdbc.user"
			response.success = { resp, reader ->
				def content = reader.text
				assertEquals('1.2 UAT jdbc.user', content);
			}
		}
	}
	
	@Test
	void get_key_not_found() {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "key/myapp/UAT/1.2/jdbc.usr"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [S-001] Cannot find key jdbc.usr for application myapp, version 1.2 and environment UAT.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}
	
	@Test
	void get_env_json_concise () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json/concise"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("1.2 UAT jdbc.user", json["jdbc.user"]); 
				assertEquals ("1.2 UAT jdbc.password", json["jdbc.password"]); 
			}
		}
	}
	
	@Test
	void get_env_json_complete () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json/complete"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("jdbc.password", json[0]["key"]);
				assertEquals ("Password used to connect to the database", json[0]["description"]); 
				assertEquals ("1.2 UAT jdbc.password", json[0]["value"]);
				assertEquals ("jdbc.user", json[1]["key"]);
				assertEquals ("User used to connect to the database", json[1]["description"]); 
				assertEquals ("1.2 UAT jdbc.user", json[1]["value"]); 
			}
		}
	}
	
	@Test
	void get_env_json_default () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("jdbc.password", json[0]["key"]);
				assertEquals ("Password used to connect to the database", json[0]["description"]); 
				assertEquals ("1.2 UAT jdbc.password", json[0]["value"]);
				assertEquals ("jdbc.user", json[1]["key"]);
				assertEquals ("User used to connect to the database", json[1]["description"]); 
				assertEquals ("1.2 UAT jdbc.user", json[1]["value"]); 
			}
		}
	}
	
	@Test
	void get_env_properties () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/properties"
			response.success = { resp, reader ->
				def content = reader.text
				assertEquals ("""# Configuration properties for 'myapp'
# Version: 1.2
# Environment: UAT

# Password used to connect to the database
jdbc.password = 1.2 UAT jdbc.password

# User used to connect to the database
jdbc.user = 1.2 UAT jdbc.user

""", content); 
			}
		}
	}
	
	@Test
	void get_env_json_unknown_variant () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/json/xxxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-003] "xxxx" variant is not supported for rendering a configuration as JSON.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}
	
	@Test
	void get_env_xml_unknown_variant () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/xml/xxxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-004] "xxxx" variant is not supported for rendering a configuration as XML.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}
	
	@Test
	void get_env_xml_attributesOnly () {
		http.get ( path: "env/myapp/UAT/1.2/xml/attributesOnly") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].@value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].@description.text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].@value.text())
			assertEquals ("User used to connect to the database", xml.param[1].@description.text())
		}
	}
	
	@Test
	void get_env_xml_default () {
		http.get ( path: "env/myapp/UAT/1.2/xml") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].@value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].@description.text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].@value.text())
			assertEquals ("User used to connect to the database", xml.param[1].@description.text())
		}
	}
	
	@Test
	void get_env_xml_mixed () {
		http.get ( path: "env/myapp/UAT/1.2/xml/mixed") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].text())
		}
	}
	
	@Test
	void get_env_xml_tagsOnly () {
		http.get ( path: "env/myapp/UAT/1.2/xml/tagsOnly") { resp, xml ->
			assertEquals ("myapp", xml.application.text())
			assertEquals ("UAT", xml.environment.text())
			assertEquals ("1.2", xml.version.text())
			assertEquals ("jdbc.password", xml.param[0].name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].description.text())
			assertEquals ("jdbc.user", xml.param[1].name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].value.text())
			assertEquals ("User used to connect to the database", xml.param[1].description.text())
		}
	}
	
	@Test
	void get_env_unknown_mode () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/xxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-002] "xxx" render mode is not defined.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}

}
