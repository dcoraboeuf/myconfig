package net.myconfig.web.language;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component("localeInterceptor")
public class LocaleInterceptor extends LocaleChangeInterceptor implements CurrentLocale {
	
	private final ThreadLocal<Locale> currentLocale = new ThreadLocal<Locale>(); 
	
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
			currentLocale.set(locale);
		}
		// Default
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public Locale getCurrentLocale() {
		Locale locale = currentLocale.get();
		return locale != null ? locale : Locale.ENGLISH;
	}

}
