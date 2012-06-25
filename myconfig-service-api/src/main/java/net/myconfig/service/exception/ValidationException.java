package net.myconfig.service.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;


import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.Iterables;

public class ValidationException extends InputException {

	protected ValidationException(Object... params) {
		
	}

}
