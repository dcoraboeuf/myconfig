package net.myconfig.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.GrantService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FunctionCacheTest extends AbstractSecurityTest {
	
	@Autowired
	private GrantService grantService;
	
	@Test
	public void userFunctionCached_tampering() throws SQLException {
		// Creates a user
		asAdmin();
		String user = userName();
		securityService.userCreate(user, "User", user + "@test.com");
		securityService.userFunctionAdd(user, UserFunction.app_create);
		// Gets the value
		assertTrue(grantService.hasUserFunction(user, UserFunction.app_create));
		// Tampering
		execute("delete from usergrants where user = ?", user);
		// Gets the value from the cache again (not changed)
		assertTrue(grantService.hasUserFunction(user, UserFunction.app_create));
	}
	
	@Test
	public void userFunctionCached_update() throws SQLException {
		// Creates a user
		asAdmin();
		String user = userName();
		securityService.userCreate(user, "User", user + "@test.com");
		securityService.userFunctionAdd(user, UserFunction.app_create);
		// Gets the value
		assertTrue(grantService.hasUserFunction(user, UserFunction.app_create));
		// Update
		asAdmin();
		securityService.userFunctionRemove(user, UserFunction.app_create);
		// Gets the value from the cache again (not changed)
		assertFalse(grantService.hasUserFunction(user, UserFunction.app_create));
	}

}
