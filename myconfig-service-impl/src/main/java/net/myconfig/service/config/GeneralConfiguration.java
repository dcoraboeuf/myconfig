package net.myconfig.service.config;

import javax.validation.Validator;

import net.myconfig.service.api.InitialisationService;

public interface GeneralConfiguration {
	
	InitialisationService initialisationService() throws Exception;
	
	Validator validator();

}
