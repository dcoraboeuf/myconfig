package net.myconfig.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {

	@Override
	public void initialize(NotBlank constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value == null) || (value.length() == 0) || StringUtils.isNotBlank(value);
	}

}
