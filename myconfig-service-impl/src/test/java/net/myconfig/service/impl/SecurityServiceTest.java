package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Ack;
import net.myconfig.core.model.UserSummary;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.exception.ValidationException;
import net.myconfig.service.security.SecurityManagementNotFoundException;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

/**
 * Test of the protection on the {@link SecurityService}.
 */
public class SecurityServiceTest extends AbstractSecurityTest {
	
	@Autowired
	private Strings strings;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Test(expected = AccessDeniedException.class)
	public void getUserList_no_auth() {
		securityService.getUserList();
	}

	@Test
	public void getUserList_admin() throws SQLException {
		asAdmin();
		List<UserSummary> users = securityService.getUserList();
		assertUserList(users);
	}

	@Test
	public void getUserList_user_granted() throws SQLException {
		asUser().grant(UserFunction.security_users);
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
	public void getUserList_user_not_granted() throws SQLException {
		asUser();
		securityService.getUserList();
	}

	@Test
	public void userCreate_admin() throws DataSetException, SQLException {
		asAdmin();
		Ack ack = securityService.userCreate("test", "Test", "test@test.com");
		assertTrue(ack.isSuccess());
		assertRecordExists("select * from users where name = 'test' and email = 'test@test.com' and password = '' and verified = false and admin = false");
	}
	
	@Test
	public void userCreate_null () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate(null, "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_blank () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("", "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_spaces () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("     ", "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: may not be blank",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_unrecognized_characters () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("<te/st\u00E9>", "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: must be sequence of ASCII letters, dash(-), underscore(_) and/or spaces ( )",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_trim () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("  test   ", "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: may not have leading or trailing blanks",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_too_long () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate(StringUtils.repeat("x", 81), "Test", "test@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-006] User name is invalid: size must be between 1 and 80",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_null () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", null);
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: may not be null",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_blank () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", "");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: size must be between 1 and 120",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_spaces () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", "   ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: not a well-formed email address",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_unrecognized_format () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", "test AT test DOT com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: not a well-formed email address",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_trim () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", "  test@test.com  ");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: not a well-formed email address",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}
	
	@Test
	public void userCreate_email_too_long () throws SQLException {
		try {
			asAdmin();
			securityService.userCreate("test", "Test", StringUtils.repeat("x", 120) + "@test.com");
			fail("Should have raised a validation error");
		} catch (ValidationException ex) {
			assertEquals (
					"[S-012] [V-007] User e-mail is invalid: size must be between 1 and 120",
					ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}

	@Test(expected = UserAlreadyDefinedException.class)
	public void userCreate_admin_already_exists() throws DataSetException, SQLException {
		asAdmin();
		securityService.userCreate("user1", "Test", "test@test.com");
		assertRecordNotExists("select * from users where name = 'user1'");
	}

	@Test
	public void userCreate_user_granted() throws DataSetException, SQLException {
		asUser().grant(UserFunction.security_users);
		Ack ack = securityService.userCreate("test", "Test", "test@test.com");
		assertTrue(ack.isSuccess());
		assertRecordExists("select * from users where name = 'test' and email = 'test@test.com' and password = '' and verified = false and admin = false");
	}

	@Test(expected = AccessDeniedException.class)
	public void userCreate_user_not_granted() throws DataSetException, SQLException {
		asUser();
		securityService.userCreate("testx", "Test", "x");
		assertRecordNotExists("select * from users where name = 'testx'");
	}
	
	@Test
	public void user_disable() throws DataSetException, SQLException {
		asAdmin();
		assertNotNull(authenticationService.getUserToken("user2", "test"));
		// Disabling
		assertRecordExists("select * from users where name = 'user2' and disabled = false");
		securityService.userDisable("user2");
		assertRecordExists("select * from users where name = 'user2' and disabled = true");
		// Cannot connect
		assertNull(authenticationService.getUserToken("user2", "test"));
		// Enable again
		securityService.userEnable("user2");
		assertRecordExists("select * from users where name = 'user2' and disabled = false");
	}

	@Test
	public void userDelete_admin() throws SQLException {
		asAdmin();
		Ack ack = securityService.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test
	public void userDelete_user_granted() throws SQLException {
		asUser().grant(UserFunction.security_users);
		Ack ack = securityService.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userDelete_user_not_granted() throws SQLException {
		asUser();
		securityService.userDelete("test");
	}

	@Test
	public void userFunctionAdd_admin() throws SQLException {
		asAdmin();
		Ack ack = securityService.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionAdd_user_granted() throws SQLException {
		asUser().grant(UserFunction.security_users);
		Ack ack = securityService.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionAdd_user_not_granted() throws SQLException {
		asUser();
		securityService.userFunctionAdd("user1", UserFunction.app_create);
	}

	@Test
	public void userFunctionRemove_admin() throws SQLException {
		asAdmin();
		Ack ack = securityService.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionRemove_user_granted() throws SQLException {
		asUser().grant(UserFunction.security_users);
		Ack ack = securityService.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionRemove_user_not_granted() throws SQLException {
		asUser();
		securityService.userFunctionRemove("user1", UserFunction.app_create);
	}

	@Test
	public void getSecurityMode_no_control() {
		assertEquals("builtin", securitySelector.getSecurityMode());
	}

	@Test
	public void setSecurityMode_admin() throws SQLException {
		asAdmin();
		securityService.setSecurityMode("none");
		assertEquals("none", securitySelector.getSecurityMode());
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void setSecurityMode_admin_unknown_mode() throws SQLException {
		asAdmin();
		securityService.setSecurityMode("xxx");
	}

	@Test
	public void setSecurityMode_user_granted() throws SQLException {
		asUser().grant(UserFunction.security_setup);
		securityService.setSecurityMode("none");
		assertEquals("none", securitySelector.getSecurityMode());
	}

	@Test(expected = AccessDeniedException.class)
	public void setSecurityMode_user_not_granted() throws SQLException {
		asUser();
		securityService.setSecurityMode("xxx");
	}

}
