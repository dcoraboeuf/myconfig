package net.myconfig.acc.client

import net.myconfig.client.java.support.AbstractClient
import net.myconfig.core.model.Message

class IntegrationTestClient extends AbstractClient {
	
	IntegrationTestClient (String url) {
		super(url)
	}

	/**
	 * GET /test/message/{email}
	 */
	def getMessage(email) {
		return get("/test/message/$email", Message.class)
	}

}
