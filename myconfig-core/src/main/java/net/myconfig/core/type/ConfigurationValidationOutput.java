package net.myconfig.core.type;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationValidationOutput {
	
	private final List<ConfigurationValueValidationOutput> validations;
	
	@JsonCreator
	public ConfigurationValidationOutput(@JsonProperty("validations") List<ConfigurationValueValidationOutput> validations) {
		this.validations = ImmutableList.copyOf(validations);
	}
	
	

}
