package net.myconfig.acc.client

import net.myconfig.client.java.Client
import net.myconfig.client.java.support.AbstractClient
import net.myconfig.client.java.support.Credentials
import net.myconfig.core.model.Ack

class SecurityClient extends AbstractClient {
	
	SecurityClient (String url) {
		super(url)
	}
	
	protected SecurityClient (String url, Credentials credentials) {
		super(url, credentials)
	}
	
	@Override
	protected Client withCredentials(Credentials credentials) {
		return new SecurityClient(getUrl(), credentials)
	}

	/**
	 * POST /security/mode/{mode}
	 */
	def setSecurityMode(mode) {
		return post("/ui/security/mode/$mode", Ack.class, null)
	}

}
