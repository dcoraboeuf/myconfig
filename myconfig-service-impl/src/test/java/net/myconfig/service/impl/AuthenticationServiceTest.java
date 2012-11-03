package net.myconfig.service.impl;

import static net.myconfig.core.AppFunction.app_config;
import static net.myconfig.core.AppFunction.app_delete;
import static net.myconfig.core.AppFunction.app_view;
import static net.myconfig.core.UserFunction.app_create;
import static net.myconfig.core.UserFunction.app_list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.User;
import net.myconfig.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationServiceTest extends AbstractIntegrationTest {

	@Autowired
	private AuthenticationService service;
	
	@Autowired
	private GrantService grantService;

	@Test
	public void admin() {
		User user = service.getUserToken("admin", "admin");
		assertNotNull(user);
		assertEquals("admin", user.getName());
		assertEquals("Administrator", user.getDisplayName());
		assertTrue(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			if (fn == app_list) {
				assertTrue(grantService.hasUserFunction("admin", fn));
			} else {
				assertFalse(grantService.hasUserFunction("admin", fn));
			}
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			if (fn == app_view) {
				assertTrue(grantService.hasAppFunction("A", "admin", fn));
			} else {
				assertFalse(grantService.hasAppFunction("A", "admin", fn));
			}
		}
		// TODO Environment functions
	}

	@Test
	public void not_verified() {
		User user = service.getUserToken("newuser", "");
		assertNull(user);
	}

	@Test
	public void not_found_password() {
		User user = service.getUserToken("user1", "xxx");
		assertNull(user);
	}

	@Test
	public void not_found_user() {
		User user = service.getUserToken("user2", "xxx");
		assertNull(user);
	}

	@Test
	public void disabled_user() throws DataSetException, SQLException {
		assertRecordExists("select * from users where name = 'disableduser' and password = '%s'", AbstractSecurityService.digest("test"));
		User user = service.getUserToken("disableduser", "test");
		assertNull(user);
	}

	@Test
	public void user1() {
		User user = service.getUserToken("user1", "test");
		assertNotNull(user);
		assertEquals("user1", user.getName());
		assertEquals("User 1", user.getDisplayName());
		assertFalse(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			if (fn == app_list || fn == app_create) {
				assertTrue(grantService.hasUserFunction("user1", fn));
			} else {
				assertFalse(grantService.hasUserFunction("user1", fn));
			}
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			if (fn == app_delete || fn == app_config || fn == app_view) {
				assertTrue(grantService.hasAppFunction("A", "user1", fn));
			} else {
				assertFalse(grantService.hasAppFunction("A", "user1", fn));
			}
		}
		// No other app
		for (AppFunction fn : AppFunction.values()) {
			assertFalse(grantService.hasAppFunction("BB", "user1", fn));
		}
		// TODO Environment functions
	}

	@Test
	public void user2() {
		User user = service.getUserToken("user2", "test");
		assertNotNull(user);
		assertEquals("user2", user.getName());
		assertEquals("User 2", user.getDisplayName());
		assertFalse(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			if (fn == app_list) {
				assertTrue(grantService.hasUserFunction("user2", fn));
			} else {
				assertFalse(grantService.hasUserFunction("user2", fn));
			}
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			if (fn == app_view) {
				assertTrue(grantService.hasAppFunction("A", "user2", fn));
			} else {
				assertFalse(grantService.hasAppFunction("A", "user2", fn));
			}
		}
		// No other app
		for (AppFunction fn : AppFunction.values()) {
			assertFalse(grantService.hasAppFunction("BB", "user2", fn));
		}
		// TODO Environment functions
	}

}
