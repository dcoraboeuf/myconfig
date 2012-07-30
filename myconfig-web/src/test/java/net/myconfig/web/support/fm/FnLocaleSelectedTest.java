package net.myconfig.web.support.fm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import net.myconfig.web.language.CurrentLocale;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class FnLocaleSelectedTest {

	private FnLocaleSelected fn;

	@Before
	public void before() {
		CurrentLocale currentLocale = mock(CurrentLocale.class);
		when(currentLocale.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		fn = new FnLocaleSelected(currentLocale);
	}

	@Test(expected = NullPointerException.class)
	public void list_null() throws TemplateModelException {
		fn.exec(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void list_too_few() throws TemplateModelException {
		fn.exec(Collections.emptyList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void list_too_much() throws TemplateModelException {
		fn.exec(Arrays.asList("a", "b"));
	}

	@Test
	public void exec_selected() throws TemplateModelException {
		boolean result = (Boolean) fn.exec(Arrays.asList("EN"));
		assertTrue(result);
	}

	@Test
	public void exec_not_selected() throws TemplateModelException {
		boolean result = (Boolean) fn.exec(Arrays.asList("FR"));
		assertFalse(result);
	}

}
