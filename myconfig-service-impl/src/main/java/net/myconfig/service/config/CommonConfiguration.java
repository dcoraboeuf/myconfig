package net.myconfig.service.config;

import java.io.IOException;

import javax.validation.Validator;

import net.sf.jstring.Strings;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class CommonConfiguration implements GeneralConfiguration {

	@Override
	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}
	
	@Bean
	public Strings strings() throws IOException {
		return new StringsLoader().load();
	}

}
