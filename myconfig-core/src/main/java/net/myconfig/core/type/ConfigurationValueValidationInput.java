package net.myconfig.core.type;

import lombok.Data;

@Data
public class ConfigurationValueValidationInput {

	private final String id;
	private final String key;
	private final String value;
	

}
