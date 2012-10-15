package net.myconfig.client.java.support;

import java.util.Locale;

import net.myconfig.core.CoreException;

public class ClientMessageException extends CoreException {

	private final String message;

	public ClientMessageException(String message) {
		super(message);
		this.message = message;
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
	
	@Override
	public String toString() {
		return message;
	}

}
