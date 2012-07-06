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
import org.junit.Test;

class ITRestUI {
	
	def version
	def http
	
	@Before
	void before() {
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
		def url = "http://localhost:$itPort/myconfig/ui/"
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
	void application() {
		// Creates a unique application
		def uuid = UUID.randomUUID().toString()
		def appName = "test_" + uuid;
		int id = application_create (appName)
		// Gets the application summary
		def summary = application_summary (id)
		assertEquals (id, summary.id)
		assertEquals (appName, summary.name)
		// TODO Basic stats
		// Deletes the application		
		application_delete (id);
		// Checks it has been deleted
		summary = application_summary(id);
		assertNull (summary);
	}
	
	def application_summary (int id) {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "applications"
			response.success = { resp, json ->
				return json.find {
					sum -> (sum.id == id) 
				}
			}
		}
	}
	
	private int application_create (String name) {
		http.request ( Method.PUT, ContentType.JSON ) {
			uri.path = "application/" + name
			response.success = { resp, json ->
				assertEquals (name, json.name)
				return json.id
			}
		}
	}
	
	private void application_delete (int id) {
		http.request ( Method.DELETE, ContentType.JSON ) {
			uri.path = "application/" + id
			response.success = { resp, json ->
				assertTrue (json.success)
			}
		}
	}

}
