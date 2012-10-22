package net.myconfig.client.java.support;

import java.util.Locale;

import net.myconfig.core.CoreException;

import org.apache.commons.lang3.ObjectUtils;

public class ClientForbiddenException extends CoreException {
	
	private final String message;

	public ClientForbiddenException(Object request) {
		super(request);
		this.message = ObjectUtils.toString(request, "");
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}
	
	@Override
	public String getLocalizedMessage(Locale locale) {
		return message;
	}

}
