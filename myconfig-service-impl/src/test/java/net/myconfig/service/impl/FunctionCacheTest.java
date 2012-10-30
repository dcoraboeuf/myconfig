package net.myconfig.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import net.myconfig.core.AppFunction;
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
		String user = createUser();
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
		String user = createUser();
		securityService.userFunctionAdd(user, UserFunction.app_create);
		// Gets the value
		assertTrue(grantService.hasUserFunction(user, UserFunction.app_create));
		// Update
		asAdmin();
		securityService.userFunctionRemove(user, UserFunction.app_create);
		// Gets the value from the cache again (not changed)
		assertFalse(grantService.hasUserFunction(user, UserFunction.app_create));
	}
	
	@Test
	public void appFunctionCached_tampering() throws SQLException {
		// Creates a user
		String user = createUser();
		int appId = myConfigService.createApplication(appName()).getId();
		securityService.appFunctionAdd(appId, user, AppFunction.app_view);
		// Gets the value
		assertTrue(grantService.hasAppFunction(appId, user, AppFunction.app_view));
		// Tampering
		execute("delete from appgrants where user = ?", user);
		// Gets the value from the cache again (not changed)
		assertTrue(grantService.hasAppFunction(appId, user, AppFunction.app_view));
	}
	
	@Test
	public void appFunctionCached_cached() throws SQLException {
		// Creates a user
		String user = createUser();
		int appId = myConfigService.createApplication(appName()).getId();
		securityService.appFunctionAdd(appId, user, AppFunction.app_view);
		// Gets the value
		assertTrue(grantService.hasAppFunction(appId, user, AppFunction.app_view));
		// Update
		grantService.appFunctionRemove(appId, user, AppFunction.app_view);
		// Gets the value from the cache again (not changed)
		assertFalse(grantService.hasAppFunction(appId, user, AppFunction.app_view));
	}

	protected String createUser() throws SQLException {
		asAdmin();
		String user = userName();
		securityService.userCreate(user, "User", user + "@test.com");
		return user;
	}

}
