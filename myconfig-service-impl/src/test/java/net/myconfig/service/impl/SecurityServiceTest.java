package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.myconfig.service.security.SecurityService;
import net.myconfig.service.security.User;
import net.myconfig.test.AbstractIntegrationTest;

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

}
