package net.myconfig.web.home;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class LocalisationControllerTest {

	private LocalisationController controller;
	private MockHttpServletResponse response;

	@Before
	public void before() {
		Strings strings = new Strings("localisation.test");
		controller = new LocalisationController(strings);

		response = new MockHttpServletResponse();
	}

	@Test
	public void no_locale() throws IOException {
		controller.localisation(null, response);
		do_locale_en();
	}

	@Test
	public void locale_en() throws IOException {
		controller.localisation(Locale.ENGLISH, response);
		do_locale_en();
	}

	protected void do_locale_en() throws UnsupportedEncodingException {
		assertEquals("text/javascript", response.getContentType());
		String content = response.getContentAsString();
		List<String> lines = new ArrayList<String>(Arrays.asList(StringUtils.split(content, "\n")));
		lines.remove(0);
		content = StringUtils.join(lines, "\n");
		assertEquals("var l = {\n" + "'application.delete': '[M-001-C] Voulez-vous vraiment supprimer l\\'application \\\"{0}\\\" et toute la configuration associ\\u00E9e ?',\n"
				+ "'applications': 'Applications configur\\u00E9es'\n" + "};", content);
	}

	@Test
	public void locale_fr() throws IOException {
		controller.localisation(Locale.FRENCH, response);
		do_locale_en();
	}

}
