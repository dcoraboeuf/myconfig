package net.myconfig.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jstring.Localizable;

@Data
@AllArgsConstructor
public class ConfigurationValueValidationResult {
	private final String id;
	private final String key;
	private final String value;
	private final Localizable message;	

	public ConfigurationValueValidationResult(ConfigurationValueValidationInput input, Localizable message) {
		this(input.getId(), input.getKey(), input.getValue(), message);
	}
	
}
