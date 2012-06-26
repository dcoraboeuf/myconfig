package net.myconfig.service.impl;

import java.util.Set;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import net.myconfig.service.exception.ValidationException;
import net.sf.jstring.LocalizableMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public abstract class AbstractDaoService extends NamedParameterJdbcDaoSupport {
	
	private final Validator validator;
	
	public AbstractDaoService(DataSource dataSource, Validator validator) {
		setDataSource(dataSource);
		this.validator = validator;
	}
	
	protected <T> ValidationException validationException (Set<ConstraintViolation<T>> violations) {
		ConstraintViolation<T> violation = violations.iterator().next();
		// Message code
		String code = String.format("%s.%s",
				violation.getRootBeanClass().getName(),
				violation.getPropertyPath());
		// Message
		Object oMessage;
		String message = violation.getMessage();
		if (StringUtils.startsWith(message, "{net.myconfig")) {
			String key = StringUtils.strip(message, "{}");
			oMessage = new LocalizableMessage(key);
		} else {
			oMessage = message;
		}
		// Exception
		return new ValidationException(new LocalizableMessage(code), oMessage, violation.getInvalidValue());
	}

	protected <T> void validate(Class<T> validationClass, String propertyName, Object value) {
		Set<ConstraintViolation<T>> violations = validator.validateValue(validationClass, propertyName, value);
		if (!violations.isEmpty()) {
			throw validationException (violations);
		}
	}

}
