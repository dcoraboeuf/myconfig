package net.myconfig.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserToken;
import net.myconfig.service.model.UserSummary;
import net.myconfig.service.security.UserAuthenticationToken;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * Test of the protection on the {@link SecurityService}.
 * 
 * TODO Create an ancestor for tests related to security
 */
// @ContextConfiguration({ "classpath:META-INF/aop.xml" })
public class SecurityServiceSecurityTest extends AbstractIntegrationTest {

	@Autowired
	public SecurityService service;

	@Before
	public void cleanContext() {
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS)));
		SecurityContextHolder.setContext(context);
	}

	@Test
	@Ignore
	public void getUserToken_nocontrol() {
		UserToken token = service.getUserToken("user1", "test");
		assertNotNull(token);
	}

	@Test(expected = AccessDeniedException.class)
	@Ignore
	public void getUserList_no_auth() {
		service.getUserList();
	}

	@Test
	@Ignore
	public void getUserList_admin() {
		asAdmin();
		List<UserSummary> users = service.getUserList();
		assertNotNull(users);
	}

	protected void asAdmin() {
		SecurityContextImpl context = new SecurityContextImpl();
		UserToken token = service.getUserToken("admin", "admin");
		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthenticationToken(token, authentication));
		SecurityContextHolder.setContext(context);
	}

	//
	// Ack userCreate(String name);
	//
	// Ack userDelete(String name);
	//
	// Ack userFunctionAdd(String name, UserFunction fn);
	//
	// Ack userFunctionRemove(String name, UserFunction fn);
	//
	// String getSecurityMode();
	//
	// void setSecurityMode(String mode);
}
