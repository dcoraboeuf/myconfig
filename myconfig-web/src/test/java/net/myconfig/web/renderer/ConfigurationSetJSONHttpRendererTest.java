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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.model.ConfigurationSet;
import net.myconfig.core.model.ConfigurationValue;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ConfigurationSetJSONHttpRendererTest {
	
	private final ConfigurationSetJSONHttpRenderer renderer = new ConfigurationSetJSONHttpRenderer();
	
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
		assertEquals ("json", renderer.getContentType());
	}
	
	@Test(expected = ConfigurationSetJSONUnknownVariantException.class)
	public void unknownVariant () throws IOException {
		renderer.renderer(new ConfigurationSet(
				"myapp",
				"UAT",
				"1.2",
				Collections.<ConfigurationValue>emptyList()), "xxx", null);
	}
	
	@Test
	public void concise () throws IOException {
		String expectedText = "{\"aaa\":\"a1\",\"bbb\":\"b2\",\"ccc\":\"c\u00e7c\"}";
		variant("concise", expectedText);
	}
	
	@Test
	public void complete () throws IOException {
		String expectedText = "[{\"key\":\"aaa\",\"description\":\"aaa description\",\"value\":\"a1\"},{\"key\":\"bbb\",\"description\":\"bbb description\",\"value\":\"b2\"},{\"key\":\"ccc\",\"description\":\"ccc accentu\u00e9e\",\"value\":\"c\u00e7c\"}]";
		variant("complete", expectedText);
	}
	
	@Test
	public void default_variant () throws IOException {
		String expectedText = "[{\"key\":\"aaa\",\"description\":\"aaa description\",\"value\":\"a1\"},{\"key\":\"bbb\",\"description\":\"bbb description\",\"value\":\"b2\"},{\"key\":\"ccc\",\"description\":\"ccc accentu\u00e9e\",\"value\":\"c\u00e7c\"}]";
		variant(null, expectedText);
	}

	protected void variant(String variant, String expectedText)
			throws IOException, UnsupportedEncodingException {
		// Set to render
		ConfigurationSet set = new ConfigurationSet(
				"myapp",
				"UAT",
				"1.2",
				Arrays.asList(
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
		renderer.renderer(set, variant, response);
		// Checks
		verify(response, times(1)).setContentType("application/json");
		verify(response, times(1)).setCharacterEncoding("UTF-8");
		// Response
		String text = bout.toString("UTF-8");
		assertEquals (expectedText, text);
	}

}
