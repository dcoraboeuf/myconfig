package net.myconfig.client.java.support;

import java.util.Locale;

import net.myconfig.core.CoreException;

public class ClientGeneralException extends CoreException {
	
	private final String message;

	public ClientGeneralException(Object request, Exception ex) {
		super(ex, request, ex);
		this.message = String.format("Error while executing %s: %s", request, ex);
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
