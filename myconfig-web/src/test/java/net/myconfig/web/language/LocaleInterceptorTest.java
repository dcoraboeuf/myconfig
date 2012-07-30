package net.myconfig.web.language;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

public class LocaleInterceptorTest {

	private LocaleInterceptor interceptor = new LocaleInterceptor();

	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
		LocaleContextHolder.setLocale(null);
	}

	@Test
	public void getCurrentLocale_null() {
		assertEquals(Locale.ENGLISH, interceptor.getCurrentLocale());
	}

	@Test
	public void getCurrentLocale_fr() {
		LocaleContextHolder.setLocale(Locale.FRENCH);
		assertEquals(Locale.FRENCH, interceptor.getCurrentLocale());
	}
	
	@Test
	public void postHandle_no_mav () throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor.postHandle(request, response, null, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void postHandle_mav_no_locale_resolver () throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = new ModelAndView("myview");
		interceptor.postHandle(request, response, null, mav);
	}
	
	@Test
	public void postHandle_mav_no_locale () throws Exception {
		LocaleResolver localeResolver = mock (LocaleResolver.class);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		ModelAndView mav = new ModelAndView("myview");
		
		interceptor.postHandle(request, response, null, mav);
		
		assertEquals("en", mav.getModel().get("locale"));
		assertEquals(Locale.ENGLISH, LocaleContextHolder.getLocale());
	}
	
	@Test
	public void postHandle_mav_locale () throws Exception {
		LocaleResolver localeResolver = mock (LocaleResolver.class);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
		

		when(localeResolver.resolveLocale(request)).thenReturn(Locale.FRENCH);
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		ModelAndView mav = new ModelAndView("myview");
		
		interceptor.postHandle(request, response, null, mav);
		
		assertEquals("fr", mav.getModel().get("locale"));
		assertEquals(Locale.FRENCH, LocaleContextHolder.getLocale());
	}

}
