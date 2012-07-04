package net.myconfig.web.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;

import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.test.Helper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import freemarker.template.Configuration;

public class ConfigurationSetXMLHttpRendererTest {
	
	private ConfigurationSetXMLHttpRenderer renderer;
	
	@Before
	public void before() throws IOException {		
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/freemarker"));
		renderer = new ConfigurationSetXMLHttpRenderer(configuration);
	}
	
	@Test
	public void appliesTo_ok () {
		assertTrue (renderer.appliesTo(ConfigurationSet.class));
	}
	
	@Test
	public void appliesTo_nok () {
		assertFalse (renderer.appliesTo(ConfigurationValue.class));
	}
	
	@Test
	public void contentType() {
		assertEquals ("xml", renderer.getContentType());
	}
	
	@Test(expected = ConfigurationSetXMLUnknownVariantException.class)
	public void unknownVariant () throws IOException {
		renderer.renderer(new ConfigurationSet(
				"myapp",
				"UAT",
				"1.2",
				Collections.<ConfigurationValue>emptyList()), "xxx", null);
	}
	
	@Test
	public void tagsOnly () throws IOException {
		variant("tagsOnly", "/renderer/xml/env-tagsOnly.xml");
	}
	
	@Test
	public void attributesOnly () throws IOException {
		variant("attributesOnly", "/renderer/xml/env-attributesOnly.xml");
	}
	
	@Test
	public void mixed () throws IOException {
		variant("mixed", "/renderer/xml/env-mixed.xml");
	}
	
	@Test
	public void default_variant () throws IOException {
		variant(null, "/renderer/xml/env-attributesOnly.xml");
	}

	protected void variant(String variant, String expectedPath)
			throws IOException, UnsupportedEncodingException {
		// Set to render
		ConfigurationSet set = new ConfigurationSet(
				"myapp",
				"UAT",
				"1.2",
				Arrays.asList(
					new ConfigurationValue("aaa", "aaa description", "a1"),
					new ConfigurationValue("bbb", "bbb description", "b2"),
					new ConfigurationValue("ccc", "ccc <accentu\u00E9e>", "c\u00e7c")
		));
		// Mock response
		MockHttpServletResponse response = new MockHttpServletResponse();
		// Call
		renderer.renderer(set, variant, response);
		// Checks
		assertEquals ("text/xml", response.getContentType());
		assertEquals ("UTF-8", response.getCharacterEncoding());
		// Response content
		String actualXml = response.getContentAsString();
		// Expected content
		String expectedXml = Helper.getResourceAsString(expectedPath);
		// Check
		assertEquals (expectedXml, actualXml);
	}

}
