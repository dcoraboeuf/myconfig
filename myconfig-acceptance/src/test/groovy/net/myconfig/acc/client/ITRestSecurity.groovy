package net.myconfig.acc.client

import net.myconfig.acc.support.AccUtils
import net.myconfig.client.java.MyConfigClient
import net.myconfig.client.java.support.ClientCannotLoginException
import net.myconfig.client.java.support.ClientForbiddenException
import net.myconfig.core.UserFunction

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration tests for the security management
 *
 */
class ITRestSecurity extends AbstractClientUseCase {
	
	/**
	 * Test client
	 */
	def itClient
	
	/**
	 * Set-up of the security
	 */
	@Before
	void setup() {
		// Creates a test client
		itClient = new IntegrationTestClient(context().getUrl())
		// Initially, security mode is 'none'
		// It means the security can be set to 'builtin' without any authentication
		client().setSecurityMode("builtin")
	}
	
	/**
	 * Cancels the set-up of the security
	 */
	@After
	void tearDown() {
		// ... and reset the security mode to 'none'
		client().withLogin("admin","admin").setSecurityMode("none")
		// Logout
		client().logout()
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
		} catch (ClientCannotLoginException ex) {
			// Expected exception
		}
		// Gets the token message for this user (integration test message box)
		def message = itClient.getMessage (userEmail)
		assert message != null
		def token = message.getContent().getToken()
		// Validates the user
		ack = client().userConfirm(userName, token, "mypassword")
		assert ack.isSuccess()
		// Connects as this user - he still cannot create an application but is now allowed to enter the application
		try {
			client().withLogin(userName, "mypassword").applicationCreate(uid("app"))
			assert false: "Should have been forbidden"
		} catch (ClientForbiddenException ex) {
			// Expected exception
		}
		// As admin, assigns the app_create function
		ack = asAdmin().userFunctionAdd(userName, UserFunction.app_create)
		assert ack.isSuccess()
		// Tests the creation
		def appName = uid("app")
		def myclient = client().withLogin(userName, "mypassword")
		myclient.applicationCreate(appName)
		// Checks the application has been created
		def applications = myclient.applications()
		// FIXME The authentication must be forced
		// because the call to some services (/ui/applications) won't automatically trigger an authentication challenge
		// FIXME assert applications.getSummaries().find { it.getName() == appName } != null
	}

}
