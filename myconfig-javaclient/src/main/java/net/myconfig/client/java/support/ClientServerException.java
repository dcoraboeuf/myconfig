package net.myconfig.client.java.support;

import net.myconfig.core.CoreException;

public class ClientServerException extends CoreException {

	public ClientServerException(Object request, int statusCode, String reasonPhrase) {
		super(request, statusCode, reasonPhrase);
	}

}
