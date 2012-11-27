package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import net.myconfig.client.java.MyConfigClient

import org.junit.Test

class ITRestUserManagement extends AbstractClientUseCase {
	
	@Test
	void userCreate() {
		def userName = uid ("user")
		def userDisplayName = uid ("user")
		def userEmail = uid ("user") + "@test.com"
		// Checks that the user is not created yet
		def user = client().users().getSummaries().find { it.getName() == userName }
		assert user == null
		// Creates the user
		def ack = client().userCreate("builtin", userName, userDisplayName, userEmail)
		assert ack.isSuccess()
		// Checks the user has been created
		user = client().users().getSummaries().find { it.getName() == userName }
		assert user != null
		assert [ userName, userDisplayName, userEmail ] == [user.getName(), user.getDisplayName(), user.getEmail()]
		assert !user.isVerified()
		assert !user.isAdmin()
		assert !user.isDisabled()
	}
	
	@Test
	void userDelete() {
		def userName = uid ("user")
		def userDisplayName = uid ("user")
		def userEmail = uid ("user") + "@test.com"
		// Checks that the user is not created yet
		def user = client().users().getSummaries().find { it.getName() == userName }
		assert user == null
		// Creates the user
		def ack = client().userCreate("builtin", userName, userDisplayName, userEmail)
		assert ack.isSuccess()
		// Checks the user has been created
		user = client().users().getSummaries().find { it.getName() == userName }
		assert user != null
		// Deletes the user
		ack = client().userDelete(userName)
		assert ack.isSuccess()
		// Checks that the user is no longer in the list
		user = client().users().getSummaries().find { it.getName() == userName }
		assert user == null
	}
	
	@Test
	void userDeleteNotFound() {
		def userName = uid ("user")
		def userDisplayName = uid ("user")
		def userEmail = uid ("user") + "@test.com"
		// Deletes the user
		def ack = client().userDelete(userName)
		assert !ack.isSuccess()
	}

}
