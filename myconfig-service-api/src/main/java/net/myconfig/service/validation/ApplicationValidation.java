package net.myconfig.service.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ApplicationValidation {
	
	@NotNull
	@Size(min = 0, max = 16)
	@Pattern(regexp = ValidationConstants.ID_REGEXP, message = ValidationConstants.ID_REGEXP_MESSAGE)
	public String id;
	
	@NotNull
	@NotBlank
	@Trimmed
	@Size(min = 1, max = 80)
	@Pattern(regexp = ValidationConstants.NAME_REGEXP, message = ValidationConstants.NAME_REGEXP_MESSAGE)
	public String name;

}
