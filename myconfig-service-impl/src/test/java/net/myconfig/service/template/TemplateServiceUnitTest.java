package net.myconfig.service.template;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;

import net.myconfig.service.api.template.TemplateModel;
import net.myconfig.service.api.template.TemplateService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateServiceUnitTest {

	private TemplateService service;
	private Configuration configuration;

	@Before
	public void init() {
		configuration = mock(Configuration.class);
		service = new TemplateServiceImpl(configuration);
	}

	@Test(expected = TemplateNotFoundException.class)
	public void generate_template_not_found() throws IOException {
		doThrow(new IOException()).when(configuration).getTemplate(anyString());
		service.generate("templateNotFound", new TemplateModel());
	}

	@Test(expected = TemplateMergeException.class)
	public void generate_template_io_exception() throws IOException, TemplateException {
		Template template = mock(Template.class);
		doThrow(new IOException()).when(template).process(anyObject(), any(Writer.class));
		when(configuration.getTemplate("mytemplate")).thenReturn(template);
		service.generate("mytemplate", new TemplateModel());
	}

	@Test(expected = TemplateMergeException.class)
	public void generate_template_template_exception() throws IOException, TemplateException {
		Template template = mock(Template.class);
		doThrow(new TemplateException(Environment.getCurrentEnvironment())).when(template).process(anyObject(), any(Writer.class));
		when(configuration.getTemplate("mytemplate")).thenReturn(template);
		service.generate("mytemplate", new TemplateModel());
	}

	@Test
	public void generate_template_ok() throws IOException, TemplateException {
		Template template = mock(Template.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Writer writer = (Writer) invocation.getArguments()[1];
				writer.write("Result");
				return null;
			}
		}).when(template).process(anyObject(), any(Writer.class));
		when(configuration.getTemplate("mytemplate")).thenReturn(template);
		String value = service.generate("mytemplate", new TemplateModel());
		assertEquals("Result", value);
	}

}
