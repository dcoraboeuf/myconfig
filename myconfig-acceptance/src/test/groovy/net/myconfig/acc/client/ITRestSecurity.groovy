package net.myconfig.acc.client

import net.myconfig.acc.support.AccUtils

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
	
	@Test
	void userFunctions() {
		println "UserFunctions"
	}

}
