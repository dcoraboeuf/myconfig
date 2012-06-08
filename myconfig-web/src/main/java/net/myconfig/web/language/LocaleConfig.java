package net.myconfig.web.language;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfig {
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleInterceptor();
	}
	
	@Bean
	public LocaleResolver localeResolver () {
		return new CookieLocaleResolver();
	}

}
