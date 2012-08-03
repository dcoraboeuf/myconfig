package net.myconfig.service.impl;

import static net.myconfig.core.AppFunction.app_delete;
import static net.myconfig.core.AppFunction.app_view;
import static net.myconfig.core.AppFunction.version_create;
import static net.myconfig.core.UserFunction.app_create;
import static net.myconfig.core.UserFunction.app_list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.security.SecurityService;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityServiceTest extends AbstractIntegrationTest {

	@Autowired
	private SecurityService service;

	@Test
	public void admin() {
		UserToken user = service.getUserToken("admin", "admin");
		assertNotNull(user);
		assertEquals("admin", user.getName());
		assertEquals("admin", user.getDisplayName());
		assertTrue(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			assertTrue(user.hasUserFunction(fn));
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			assertTrue(user.hasAppFunction(1, fn));
		}
		// TODO Environment functions
	}

	@Test
	public void not_found_password() {
		UserToken user = service.getUserToken("user1", "xxx");
		assertNull(user);
	}

	@Test
	public void not_found_user() {
		UserToken user = service.getUserToken("user2", "xxx");
		assertNull(user);
	}

	@Test
	public void user1() {
		UserToken user = service.getUserToken("user1", "test");
		assertNotNull(user);
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getDisplayName());
		assertFalse(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			if (fn == app_list || fn == app_create) {
				assertTrue(user.hasUserFunction(fn));
			} else {
				assertFalse(user.hasUserFunction(fn));
			}
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			if (fn == app_delete || fn == version_create || fn == app_view) {
				assertTrue(user.hasAppFunction(1, fn));
			} else {
				assertFalse(user.hasAppFunction(1, fn));
			}
		}
		// No other app
		for (AppFunction fn : AppFunction.values()) {
			assertFalse(user.hasAppFunction(2, fn));
		}
		// TODO Environment functions
	}

	@Test
	public void user2() {
		UserToken user = service.getUserToken("user2", "test");
		assertNotNull(user);
		assertEquals("user2", user.getName());
		assertEquals("user2", user.getDisplayName());
		assertFalse(user.isAdmin());
		// User functions
		for (UserFunction fn : UserFunction.values()) {
			if (fn == app_list) {
				assertTrue(user.hasUserFunction(fn));
			} else {
				assertFalse(user.hasUserFunction(fn));
			}
		}
		// Applications functions
		for (AppFunction fn : AppFunction.values()) {
			if (fn == app_view) {
				assertTrue(user.hasAppFunction(1, fn));
			} else {
				assertFalse(user.hasAppFunction(1, fn));
			}
		}
		// No other app
		for (AppFunction fn : AppFunction.values()) {
			assertFalse(user.hasAppFunction(2, fn));
		}
		// TODO Environment functions
	}

}
