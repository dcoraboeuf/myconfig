package net.myconfig.service.config;

import java.io.IOException;

import javax.validation.Validator;

import net.myconfig.core.utils.StringsLoader;
import net.sf.jstring.Strings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class CommonConfiguration {

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}
	
	@Bean
	public Strings strings() throws IOException {
		return new StringsLoader().load();
	}

}
