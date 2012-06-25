package net.myconfig.service.impl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ApplicationValidation {
	
	@NotNull
	@Size(min = 1, max = 80)
	public String name;

}
