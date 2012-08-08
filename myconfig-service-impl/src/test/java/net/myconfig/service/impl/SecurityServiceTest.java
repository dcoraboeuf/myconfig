package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.SecurityManagementNotFoundException;
import net.myconfig.service.security.UserAlreadyDefinedException;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

/**
 * Test of the protection on the {@link SecurityService}.
 */
public class SecurityServiceTest extends AbstractSecurityTest {

	@Test(expected = AccessDeniedException.class)
	public void getUserList_no_auth() {
		securityService.getUserList();
	}

	@Test
	public void getUserList_admin() {
		asAdmin();
		List<UserSummary> users = securityService.getUserList();
		assertUserList(users);
	}

	@Test
	public void getUserList_user_granted() {
		asUser(UserFunction.security_users);
		List<UserSummary> users = securityService.getUserList();
		assertUserList(users);
	}

	private void assertUserList(List<UserSummary> users) {
		assertNotNull(users);
		assertEquals(3, users.size());
		assertUser(users.get(0), "admin", true, UserFunction.app_list);
		assertUser(users.get(1), "user1", false, UserFunction.app_create, UserFunction.app_list);
		assertUser(users.get(2), "user2", false, UserFunction.app_list);

	}

	private void assertUser(UserSummary user, String name, boolean admin, UserFunction... expectedFunctions) {
		assertNotNull(user);
		assertEquals(name, user.getName());
		assertEquals(admin, user.isAdmin());
		assertEquals(EnumSet.copyOf(Arrays.asList(expectedFunctions)), user.getFunctions());
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

	@Test(expected = UserAlreadyDefinedException.class)
	public void userCreate_admin_already_exists() {
		asAdmin();
		securityService.userCreate("user1");
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
		assertEquals("builtin", securitySelector.getSecurityMode());
	}

	@Test
	public void setSecurityMode_admin() {
		asAdmin();
		securityService.setSecurityMode("none");
		assertEquals("none", securitySelector.getSecurityMode());
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void setSecurityMode_admin_unknown_mode() {
		asAdmin();
		securityService.setSecurityMode("xxx");
	}

	@Test
	public void setSecurityMode_user_granted() {
		asUser(UserFunction.security_setup);
		securityService.setSecurityMode("none");
		assertEquals("none", securitySelector.getSecurityMode());
	}

	@Test(expected = AccessDeniedException.class)
	public void setSecurityMode_user_not_granted() {
		asUser();
		securityService.setSecurityMode("xxx");
	}

}
