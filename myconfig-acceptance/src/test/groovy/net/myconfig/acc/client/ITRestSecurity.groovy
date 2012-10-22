package net.myconfig.acc.client

import net.myconfig.acc.support.AccUtils
import net.myconfig.client.java.MyConfigClient
import net.myconfig.client.java.support.ClientAuthorizationException

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration tests for the security management
 *
 */
class ITRestSecurity extends AbstractClientUseCase {
	
	/**
	 * Security client
	 */
	def security
	
	/**
	 * Set-up of the security
	 */
	@Before
	void setup() {
		// Creates a security client
		security = new SecurityClient(AccUtils.CONTEXT.getUrl())
		// Initially, security mode is 'none'
		// It means the security can be set to 'builtin' without any authentication
		security.setSecurityMode("builtin")
	}
	
	/**
	 * Cancels the set-up of the security
	 */
	@After
	void tearDown() {
		// ... and reset the security mode to 'none'
		security.withLogin("admin","admin").setSecurityMode("none")
		// Logout
		security.logout()
	}
	
	MyConfigClient asAdmin () {
		return client().withLogin("admin", "admin")
	}
	
	@Test
	void userCreationProcess() {
		// Creates a user
		def userName = uid ("user")
		def userDisplayName = uid ("user")
		def userEmail = uid ("user") + "@test.com"
		def ack = asAdmin().userCreate(userName, userDisplayName, userEmail)
		assert ack.isSuccess()
		// Tries to log - we do not have any password yet
		try {
			client().withLogin(userName, "mypassword").applicationCreate(uid("app"))
			assert false: "Authentication should have failed"
		} catch (ClientAuthorizationException ex) {
			// Expected exception
		}
		// TODO Gets the token message for this user (integration test message box)
		// TODO Validates the user
		// TODO Connects as this user
	}

}
