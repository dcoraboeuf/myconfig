package net.myconfig.acc.client

import net.myconfig.client.java.Client
import net.myconfig.client.java.support.AbstractClient
import net.myconfig.client.java.support.Credentials
import net.myconfig.core.model.Message

class IntegrationTestClient extends AbstractClient {
	
	IntegrationTestClient (String url) {
		super(url)
	}
	
	protected IntegrationTestClient (String url, Credentials credentials) {
		super(url, credentials)
	}
	
	@Override
	protected Client withCredentials(Credentials credentials) {
		return new IntegrationTestClient(getUrl(), credentials)
	}

	/**
	 * GET /test/message/{email}
	 */
	def getMessage(email) {
		return get("/test/message/$email", Message.class)
	}

}
