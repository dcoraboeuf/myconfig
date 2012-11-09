package net.myconfig.core.type;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jstring.Strings;

@Data
@AllArgsConstructor
public class ConfigurationValueValidationOutput {
	private final String id;
	private final String key;
	private final String value;
	private final String message;

	public ConfigurationValueValidationOutput(ConfigurationValueValidationResult result, Strings strings, Locale locale) {
		this(result.getId(), result.getKey(), result.getValue(), result.getMessage() != null ? result.getMessage().getLocalizedMessage(strings, locale) : "");
	}

}
