package net.myconfig.service.security;

import net.myconfig.core.CoreException;

public class GrantParamAlreadyDefinedException extends CoreException {

	public GrantParamAlreadyDefinedException(String name, Class<?> annotationClass) {
		super(name, annotationClass.getSimpleName());
	}

}
