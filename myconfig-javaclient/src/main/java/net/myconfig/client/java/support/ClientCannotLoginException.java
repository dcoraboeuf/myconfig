package net.myconfig.client.java.support;

import org.apache.commons.lang3.ObjectUtils;

public class ClientCannotLoginException extends ClientMessageException {

	public ClientCannotLoginException(Object request) {
		super(ObjectUtils.toString(request, ""));
	}

}
