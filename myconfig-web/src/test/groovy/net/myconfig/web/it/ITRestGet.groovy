package net.myconfig.web.it

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

import net.sf.json.JSONNull

import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

class ITRestGet {
	
	def version
	def http
	
	@Before
	void before() {
		def itPort = System.properties['it.port']
		if (itPort == null || itPort == "") {
			itPort = "9999"
		}
		println ("it.port = $itPort")
		def props = new Properties()
		getClass().getResourceAsStream("/Project.properties").withStream {
			stream -> props.load(stream)
		}
		version = props["project.version"]
		println("Version = $version")
		// URL of the server
		def url = "http://localhost:$itPort/myconfig/get/"
		println ("URL = $url")
		// Creates the HTTP client for the API
		http = new HTTPBuilder(url)
	}
	
	@Test
	@Ignore
	void version() {
		http.get ( path: 'version' ) { resp, json ->
			println("Response status : $resp.status")
			println("Response content: $json")
			def actualVersion = json.versionNumber
			if (actualVersion != version) {
				fail("Expected version $version but was $actualVersion")
			}
		}
	}
	
	@Test
	void get_key_ok() {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "key/jdbc.user/myapp/1.2/UAT"
			response.success = { resp, reader ->
				def content = reader.text
				println("Response status : $resp.status")
				println("Response content: $content")
				assertEquals('1.2 UAT jdbc.user', content);
			}
		}
	}

}
