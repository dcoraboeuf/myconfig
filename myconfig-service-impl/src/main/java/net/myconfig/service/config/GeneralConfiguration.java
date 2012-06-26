package net.myconfig.service.config;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.ConfigurationService;

public interface GeneralConfiguration {
	
	ConfigurationService configurationService() throws Exception;
	
	DataSource dataSource() throws Exception;
	
	Validator validator();

}
