package net.myconfig.core;

import net.sf.jstring.LocalizableException;

public abstract class CoreException extends LocalizableException {

	private static final long serialVersionUID = 1L;

	public CoreException(Object... params) {
		super(CoreException.class.getName(), params);
	}

	public CoreException(Throwable error, Object... params) {
		super(CoreException.class.getName(), error, params);
	}

	public CoreException() {
		super(CoreException.class.getName());
	}

	@Override
	public String getCode() {
		return getClass().getName();
	}

}
