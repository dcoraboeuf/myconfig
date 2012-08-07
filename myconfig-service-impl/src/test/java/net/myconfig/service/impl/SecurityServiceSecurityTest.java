package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.UserAuthenticationToken;
import net.myconfig.test.AbstractIntegrationTest;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test of the protection on the {@link SecurityService}.
 * 
 * TODO Create an ancestor for tests related to security
 */
@ContextConfiguration({ "classpath:META-INF/secure-aop.xml" })
public class SecurityServiceSecurityTest extends AbstractIntegrationTest {

	@Autowired
	public SecurityService service;

	@Before
	public void cleanContext() {
		// Sets the security mode
		asAdmin();
		service.setSecurityMode("builtin");
		// No context
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS)));
		SecurityContextHolder.setContext(context);
	}

	@Test
	public void getUserToken_nocontrol() {
		UserToken token = service.getUserToken("user1", "test");
		assertNotNull(token);
	}

	@Test(expected = AccessDeniedException.class)
	public void getUserList_no_auth() {
		service.getUserList();
	}

	@Test
	public void getUserList_admin() {
		asAdmin();
		List<UserSummary> users = service.getUserList();
		assertNotNull(users);
	}

	@Test
	public void getUserList_user_granted() {
		asUser(UserFunction.security_users);
		List<UserSummary> users = service.getUserList();
		assertNotNull(users);
	}

	@Test(expected = AccessDeniedException.class)
	public void getUserList_user_not_granted() {
		asUser();
		service.getUserList();
	}

	@Test
	public void userCreate_admin() {
		asAdmin();
		Ack ack = service.userCreate("test");
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userCreate_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = service.userCreate("test");
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userCreate_user_not_granted() {
		asUser();
		service.userCreate("test");
	}

	@Test
	public void userDelete_admin() {
		asAdmin();
		Ack ack = service.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test
	public void userDelete_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = service.userDelete("test");
		assertFalse(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userDelete_user_not_granted() {
		asUser();
		service.userDelete("test");
	}

	@Test
	public void userFunctionAdd_admin() {
		asAdmin();
		Ack ack = service.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionAdd_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = service.userFunctionAdd("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionAdd_user_not_granted() {
		asUser();
		service.userFunctionAdd("user1", UserFunction.app_create);
	}

	@Test
	public void userFunctionRemove_admin() {
		asAdmin();
		Ack ack = service.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test
	public void userFunctionRemove_user_granted() {
		asUser(UserFunction.security_users);
		Ack ack = service.userFunctionRemove("user1", UserFunction.app_create);
		assertTrue(ack.isSuccess());
	}

	@Test(expected = AccessDeniedException.class)
	public void userFunctionRemove_user_not_granted() {
		asUser();
		service.userFunctionRemove("user1", UserFunction.app_create);
	}

	@Test
	public void getSecurityMode_no_control() {
		assertEquals("builtin", service.getSecurityMode());
	}

	@Test
	public void setSecurityMode_admin() {
		asAdmin();
		service.setSecurityMode("xxx");
		assertEquals("xxx", service.getSecurityMode());
	}

	@Test
	public void setSecurityMode_user_granted() {
		asUser(UserFunction.security_setup);
		service.setSecurityMode("xxx");
		assertEquals("xxx", service.getSecurityMode());
	}

	@Test(expected = AccessDeniedException.class)
	public void setSecurityMode_user_not_granted() {
		asUser();
		service.setSecurityMode("xxx");
	}

	protected void asUser(final UserFunction... functions) {
		SecurityContextImpl context = new SecurityContextImpl();

		UserToken token = mock(UserToken.class);
		when(token.hasUserFunction(any(UserFunction.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				UserFunction fn = (UserFunction) invocation.getArguments()[0];
				return ArrayUtils.contains(functions, fn);
			}
		});

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthenticationToken(token, authentication));
		SecurityContextHolder.setContext(context);
	}

	protected void asAdmin() {
		SecurityContextImpl context = new SecurityContextImpl();
		UserToken token = service.getUserToken("admin", "admin");
		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthenticationToken(token, authentication));
		SecurityContextHolder.setContext(context);
	}
}
