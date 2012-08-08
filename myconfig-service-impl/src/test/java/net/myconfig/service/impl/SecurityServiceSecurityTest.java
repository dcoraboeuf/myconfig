package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

/**
 * Test of the protection on the {@link SecurityService}.
 */
public class SecurityServiceSecurityTest extends AbstractSecurityTest {

	@Test(expected = AccessDeniedException.class)
	public void getUserList_no_auth() {
		securityService.getUserList();
	}

	@Test
	public void getUserList_admin() {
		asAdmin();
		List<UserSummary> users = securityService.getUserList();
		assertNotNull(users);
	}

	@Test
	public void getUserList_user_granted() {
		asUser(UserFunction.security_users);
		List<UserSummary> users = securityService.getUserList();
		assertNotNull(users);
	}

	@Test(expected = AccessDeniedException.class)
	public void getUserList_user_not_granted() {
		asUser();
		securityService.getUserList();
	}

	@Test
	public void userCreate_admin() {
		asAdmin();
		Ack ack = securityService.userCreate("test");
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userCreate_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = securityService.userCreate("test");
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userCreate_user_not_granted() {
		asUser();
		securityService.userCreate("test");
	}

	@Test
	public void userDelete_admin() {
		asAdmin();
		Ack ack = securityService.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test
	public void userDelete_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = securityService.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userDelete_user_not_granted() {
		asUser();
		securityService.userDelete("test");
	}

	@Test
	public void userFunctionAdd_admin() {
		asAdmin();
		Ack ack = securityService.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionAdd_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = securityService.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionAdd_user_not_granted() {
		asUser();
		securityService.userFunctionAdd("user1", UserFunction.app_create);
	}

	@Test
	public void userFunctionRemove_admin() {
		asAdmin();
		Ack ack = securityService.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionRemove_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = securityService.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionRemove_user_not_granted() {
		asUser();
		securityService.userFunctionRemove("user1", UserFunction.app_create);
	}

	@Test
	public void getSecurityMode_no_control() {
		assertEquals("builtin", securityService.getSecurityMode());
	}

	@Test
	public void setSecurityMode_admin() {
		asAdmin();
		securityService.setSecurityMode("none");
		assertEquals("none", securityService.getSecurityMode());
	}

	@Test
	public void setSecurityMode_user_granted() {
		asUser(UserFunction.security_setup);
		securityService.setSecurityMode("none");
		assertEquals("none", securityService.getSecurityMode());
	}

	@Test(expected = AccessDeniedException.class)
	public void setSecurityMode_user_not_granted() {
		asUser();
		securityService.setSecurityMode("xxx");
	}

}
