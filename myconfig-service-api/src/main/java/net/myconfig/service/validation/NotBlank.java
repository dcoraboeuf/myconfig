package net.myconfig.service.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NotBlankValidator.class)
public @interface NotBlank {

	String message() default "{net.myconfig.service.validation.NotBlank.message}";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default {};
}
