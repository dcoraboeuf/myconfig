package net.myconfig.service.impl;

import static java.util.Arrays.asList;
import static net.myconfig.core.UserFunction.app_create;
import static net.myconfig.core.UserFunction.app_list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.security.SecurityService;
import net.myconfig.service.security.User;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityServiceTest extends AbstractIntegrationTest {

	@Autowired
	private SecurityService service;

	@Test
	public void admin() {
		User user = service.getUser("admin", "admin");
		assertNotNull(user);
		assertEquals("admin", user.getName());
		assertTrue(user.isAdmin());
	}

	@Test
	public void not_found_password() {
		User user = service.getUser("user1", "xxx");
		assertNull(user);
	}

	@Test
	public void not_found_user() {
		User user = service.getUser("user2", "xxx");
		assertNull(user);
	}

	@Test
	public void user() {
		User user = service.getUser("user1", "test");
		assertNotNull(user);
		assertEquals("user1", user.getName());
		assertFalse(user.isAdmin());
	}

	@Test
	public void admin_user_functions() {
		List<UserFunction> functions = service.getUserFunctions(createAdminUser("admin"));
		assertEquals(asList(UserFunction.values()), functions);
	}

	@Test
	public void admin_user1_functions() {
		List<UserFunction> functions = service.getUserFunctions(createUser("user1"));
		assertEquals(asList(app_create, app_list), functions);
	}

	@Test
	public void admin_user2_functions() {
		List<UserFunction> functions = service.getUserFunctions(createUser("user2"));
		assertEquals(asList(app_list), functions);
	}

	private User createAdminUser(String name) {
		return new User(name, true);
	}

	private User createUser(String name) {
		return new User(name, false);
	}

}
