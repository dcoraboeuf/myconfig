package net.myconfig.web.language;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component("localeInterceptor")
public class LocaleInterceptor extends LocaleChangeInterceptor implements CurrentLocale {
	
	private final Logger logger = LoggerFactory.getLogger(LocaleInterceptor.class);
	
	public LocaleInterceptor() {
		setParamName("language");
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			// Gets the current locale resolver
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
			}
			// Gets the selected locale
			Locale locale = localeResolver.resolveLocale(request);
			if (locale == null) {
				locale = Locale.ENGLISH;
			}
			// Sets the locale in the model
			modelAndView.addObject("locale", locale.toString());
			// Sets the locale in the context
			LocaleContextHolder.setLocale(locale, true);
			logger.debug("Setting the locale to " + locale);
		}
		// Default
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public Locale getCurrentLocale() {
		Locale locale = LocaleContextHolder.getLocale();
		return locale != null ? locale : Locale.ENGLISH;
	}

}
