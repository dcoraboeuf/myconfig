package net.myconfig.service.validation;

public interface ValidationConstants {
	
	String ID_REGEXP = "[a-zA-Z][a-zA-Z0-9_]+";
	
	String ID_REGEXP_MESSAGE = "{net.myconfig.service.validation.ValidationConstants.id}";
	
	String NAME_REGEXP = "[\\w\\-\\. ]*";
	
	String NAME_REGEXP_MESSAGE = "{net.myconfig.service.validation.ValidationConstants.name}";

}
