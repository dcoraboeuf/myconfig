package net.myconfig.service.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class KeyValidation {

	@NotNull
	@NotBlank
	@Size(min = 1, max = 80)
	public String name;

	@NotBlank
	@Size(max = 500)
	public String description;

}
