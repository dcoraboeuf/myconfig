package net.myconfig.client.java.support;

import org.apache.commons.lang3.ObjectUtils;

public class ClientForbiddenException extends ClientMessageException {

	public ClientForbiddenException(Object request) {
		super(ObjectUtils.toString(request, ""));
	}

}
