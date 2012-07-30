package net.myconfig.web.support.fm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import net.myconfig.web.language.CurrentLocale;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class FnLocTest {

	private FnLoc fn;

	@Before
	public void before() {
		Strings strings = new Strings("META-INF.resources.web-messages");
		CurrentLocale currentLocale = mock(CurrentLocale.class);
		when(currentLocale.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		fn = new FnLoc(strings, currentLocale);
	}

	@Test(expected = NullPointerException.class)
	public void list_null() throws TemplateModelException {
		fn.exec(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void list_too_few() throws TemplateModelException {
		fn.exec(Collections.emptyList());
	}
	
	@Test
	public void exec_no_param () throws TemplateModelException {
		String result = (String) fn.exec(Arrays.asList("configuration.changes.submit.error"));
		assertEquals ("[M-005-E] Could not update the configuration", result);
	}
	
	@Test
	public void exec_no_params () throws TemplateModelException {
		String result = (String) fn.exec(Arrays.asList("version.delete", "V1"));
		assertEquals ("[M-002-C] Do you want to delete the \"V1\" version and all its associated configuration?", result);
	}

}
