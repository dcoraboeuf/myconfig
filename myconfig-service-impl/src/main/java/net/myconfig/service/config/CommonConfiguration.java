package net.myconfig.service.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class CommonConfiguration implements GeneralConfiguration {

	@Override
	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

}
