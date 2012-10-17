package net.myconfig.client.java.support;

import java.util.Locale;

import net.myconfig.core.CoreException;

public class ClientServerException extends CoreException {
	
	private final String message;

	public ClientServerException(Object request, int statusCode, String reasonPhrase) {
		super(request, statusCode, reasonPhrase);
		this.message = String.format("%s [%d] %s", request, statusCode, reasonPhrase);
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
