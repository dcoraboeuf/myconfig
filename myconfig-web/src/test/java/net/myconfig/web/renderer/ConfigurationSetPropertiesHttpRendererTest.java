package net.myconfig.web.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationValue;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ConfigurationSetPropertiesHttpRendererTest {
	
	private final ConfigurationSetPropertiesHttpRenderer renderer = new ConfigurationSetPropertiesHttpRenderer();
	
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
		assertEquals ("properties", renderer.getContentType());
	}
	
	@Test
	public void properties () throws IOException {
		// Set to render
		ConfigurationSet set = new ConfigurationSet(
				"myapp",
				"UAT",
				"1.2",Arrays.asList(
					new ConfigurationValue("aaa", "aaa description", "a1"),
					new ConfigurationValue("bbb", "bbb description", "b2"),
					new ConfigurationValue("ccc", "ccc accentu\u00E9e", "c\u00e7c")
		));
		// Mock output
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ServletOutputStream output = mock (ServletOutputStream.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				byte[] bytes = (byte[]) invocation.getArguments()[0];
				bout.write(bytes);
				return null;
			}
		}).when(output).write(any(byte[].class));
		// Mock response
		HttpServletResponse response = mock (HttpServletResponse.class);
		when (response.getOutputStream()).thenReturn (output);
		// Call
		renderer.renderer(set, null, response);
		// Checks
		verify(response, times(1)).setContentType("text/plain");
		verify(response, times(1)).setCharacterEncoding("US-ASCII");
		// Response
		String text = bout.toString("US-ASCII");
		assertEquals ("# Configuration properties for 'myapp'\n# Version: 1.2\n# Environment: UAT\n\n# aaa description\naaa = a1\n\n# bbb description\nbbb = b2\n\n# ccc accentu\\u00E9e\nccc = c\\u00E7c\n\n", text);
	}

}
