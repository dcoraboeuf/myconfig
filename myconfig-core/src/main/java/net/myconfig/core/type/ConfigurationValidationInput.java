package net.myconfig.core.type;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

@Data
public class ConfigurationValidationInput {
	
	private final List<ConfigurationValueValidationInput> validations;
	
	@JsonCreator
	public ConfigurationValidationInput(@JsonProperty("validations") List<ConfigurationValueValidationInput> validations) {
		this.validations = ImmutableList.copyOf(validations);
	}
	
	

}
