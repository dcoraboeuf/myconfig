package net.myconfig.service.config;

import javax.validation.Validator;

import net.myconfig.service.api.ConfigurationService;

public interface GeneralConfiguration {
	
	ConfigurationService configurationService() throws Exception;
	
	Validator validator();

}
