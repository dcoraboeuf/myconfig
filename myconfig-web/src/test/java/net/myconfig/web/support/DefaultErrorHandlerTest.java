package net.myconfig.web.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.VersionAlreadyDefinedException;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class DefaultErrorHandlerTest {

	private static Strings strings;

	@BeforeClass
	public static void beforeClass() {
		strings = new Strings(true, "META-INF.resources.exceptions", "META-INF.resources.web-exceptions", "META-INF.resources.model");
	}

	private DefaultErrorHandler handler;

	@Before
	public void before() {
		handler = new DefaultErrorHandler(strings, null);
	}

	@Test
	public void input() {
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage message = handler.handleError(request, Locale.ENGLISH, new VersionAlreadyDefinedException("A", "version"));
		assertMessage(message, "[S-003] The version \"version\" is already defined.");
	}

	@Test
	public void core() {
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage message = handler.handleError(request, Locale.ENGLISH, new ApplicationNotFoundException("A"));
		assertMessage(message, "[S-004] Cannot find application A");
	}

	@Test
	public void localizable() {
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage message = handler.handleError(request, Locale.ENGLISH, new LocalizableException("model.application.name"));
		assertMessage(message, "Name");
	}

	@Test
	public void non_localizable() {
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage message = handler.handleError(request, Locale.ENGLISH, new SQLException("Sensible details"));
		assertMessage(message, "[W-000] A general error has occured.");
	}

	protected void assertMessage(ErrorMessage message, String text) {
		assertNotNull(message);
		assertEquals(36, message.getUuid().length());
		assertEquals(text, message.getMessage());
	}

}
