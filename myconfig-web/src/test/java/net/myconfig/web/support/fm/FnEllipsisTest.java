package net.myconfig.web.support.fm;

import static java.util.Locale.ENGLISH;
import static net.myconfig.web.support.fm.FnEllipsis.ELLIPSIS;
import static net.myconfig.web.support.fm.FnEllipsis.ellipsis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.myconfig.web.language.CurrentLocale;

import org.junit.Test;

import freemarker.template.TemplateModelException;

public class FnEllipsisTest {

	private static final String SENTENCE = "This is a long sentence.";
	private static final String SENTENCE_ELLIPSED_3 = "Th" + ELLIPSIS;
	private static final String SENTENCE_ELLIPSED_5 = "This" + ELLIPSIS;
	private static final String SENTENCE_ELLIPSED_10 = "This is a" + ELLIPSIS;
	private static final String SENTENCE_ELLIPSED_20 = "This is a long" + ELLIPSIS;

	@Test(expected = NullPointerException.class)
	public void ellipsis_no_locale() {
		assertNull(ellipsis(null, null, 0));
	}

	@Test
	public void ellipsis_null() {
		assertNull(ellipsis(ENGLISH, null, 0));
	}

	@Test
	public void ellipsis_length_ok() {
		assertEquals(SENTENCE, ellipsis(ENGLISH, SENTENCE, 30));
	}

	@Test
	public void ellipsis_20() {
		assertEquals(SENTENCE_ELLIPSED_20, ellipsis(ENGLISH, SENTENCE, 20));
	}

	@Test
	public void ellipsis_10() {
		assertEquals(SENTENCE_ELLIPSED_10, ellipsis(ENGLISH, SENTENCE, 10));
	}

	@Test
	public void ellipsis_5() {
		assertEquals(SENTENCE_ELLIPSED_5, ellipsis(ENGLISH, SENTENCE, 5));
	}

	@Test
	public void ellipsis_3() {
		assertEquals(SENTENCE_ELLIPSED_3, ellipsis(ENGLISH, SENTENCE, 3));
	}

	@Test(expected = NullPointerException.class)
	public void exec_no_list() throws TemplateModelException {
		FnEllipsis fn = new FnEllipsis(null);
		fn.exec(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void exec_args_too_few() throws TemplateModelException {
		FnEllipsis fn = new FnEllipsis(null);
		fn.exec(Arrays.asList("test"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void exec_args_too_much() throws TemplateModelException {
		FnEllipsis fn = new FnEllipsis(null);
		fn.exec(Arrays.<Object> asList("test", 1, 2));
	}

	@Test(expected = NumberFormatException.class)
	public void exec_no_number() throws TemplateModelException {
		FnEllipsis fn = new FnEllipsis(null);
		fn.exec(Arrays.<Object> asList(SENTENCE, "mmmm"));
	}

	@Test(expected = ClassCastException.class)
	public void exec_no_string() throws TemplateModelException {
		FnEllipsis fn = new FnEllipsis(null);
		fn.exec(Arrays.<Object> asList(SENTENCE, 10));
	}

	@Test
	public void exec_ok() throws TemplateModelException {
		CurrentLocale currentLocale = mock(CurrentLocale.class);
		when(currentLocale.getCurrentLocale()).thenReturn(ENGLISH);
		FnEllipsis fn = new FnEllipsis(currentLocale);
		String result = (String) fn.exec(Arrays.<Object> asList(SENTENCE, "10"));
		assertEquals(SENTENCE_ELLIPSED_10, result);
	}

}
