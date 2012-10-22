package net.myconfig.acc.client

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * Integration tests for the security management
 *
 */
class ITRestSecurity {
	
	/**
	 * Set-up of the security
	 */
	@BeforeClass
	static void setup() {
		// Initially, security mode is 'none'
		// FIXME It means the security can be set to 'builtin' without any authentication
	}
	
	/**
	 * Cancels the set-up of the security
	 */
	@AfterClass
	static void tearDown() {
		// FIXME Logs as 'admin' and reset the security mode to 'none'
	}
	
	@Test
	void userFunctions() {
		println "UserFunctions"
	}

}
