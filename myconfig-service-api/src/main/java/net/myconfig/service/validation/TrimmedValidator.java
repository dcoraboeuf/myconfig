package net.myconfig.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class TrimmedValidator implements ConstraintValidator<Trimmed, String> {

	@Override
	public void initialize(Trimmed constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.isBlank(value) || StringUtils.equals(StringUtils.trim(value), value);
	}

}
