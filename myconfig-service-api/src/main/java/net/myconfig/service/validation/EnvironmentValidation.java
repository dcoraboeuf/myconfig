package net.myconfig.service.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EnvironmentValidation {

	@NotNull
	@NotBlank
	@Trimmed
	@Size(min = 1, max = 80)
	public String name;

}
