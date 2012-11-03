package net.myconfig.service.validation;

public interface ValidationConstants {
	
	String ID_REGEXP = "[a-zA-Z]+";
	
	String ID_REGEXP_MESSAGE = "{net.myconfig.service.validation.ValidationConstants.id}";
	
	String NAME_REGEXP = "[\\w\\-\\. ]*";
	
	String NAME_REGEXP_MESSAGE = "{net.myconfig.service.validation.ValidationConstants.name}";

}
