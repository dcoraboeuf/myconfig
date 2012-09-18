package net.myconfig.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;

import net.myconfig.core.MyConfigRoles;
import net.myconfig.service.api.security.User;
import net.myconfig.service.support.UserBuilder;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserAuthenticationTokenTest {

	@Test
	public void name() {
		User token = UserBuilder.create("x").build();
		Authentication authentication = mock(Authentication.class);

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);

		assertEquals("x", userAuthentication.getName());
	}

	@Test
	public void credentials() {
		User token = UserBuilder.user("xxx");
		Authentication authentication = mock(Authentication.class);
		when(authentication.getCredentials()).thenReturn("xxx");

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);

		assertEquals("xxx", userAuthentication.getCredentials());
		verify(authentication, times(1)).getCredentials();
	}

	@Test
	public void principal() {
		User token = UserBuilder.user("ppp");
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn("ppp");

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);

		assertEquals("ppp", userAuthentication.getPrincipal());
		verify(authentication, times(1)).getPrincipal();
	}

	@Test
	public void details() {
		User token = UserBuilder.user();
		Authentication authentication = mock(Authentication.class);

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);

		assertSame(token, userAuthentication.getDetails());
		verify(authentication, never()).getDetails();
	}

	@Test
	public void authenticated() {
		User token = UserBuilder.user();
		Authentication authentication = mock(Authentication.class);

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);

		assertTrue(userAuthentication.isAuthenticated());
		verify(authentication, never()).isAuthenticated();
	}

	@Test
	public void authenticated_set() {
		User token = UserBuilder.user();
		Authentication authentication = mock(Authentication.class);

		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);
		userAuthentication.setAuthenticated(true);
		userAuthentication.setAuthenticated(false);
		verify(authentication, never()).setAuthenticated(false);
		verify(authentication, never()).setAuthenticated(true);
	}

	@Test
	public void role_admin() {
		User token = UserBuilder.create("admin").admin().build();
		testRole(token, MyConfigRoles.ADMIN);
	}

	@Test
	public void role_user() {
		User token = UserBuilder.user();
		testRole(token, MyConfigRoles.USER);
	}

	@Test
	public void role_anonymous() {
		User token = null;
		testRole(token, MyConfigRoles.ANONYMOUS);
	}

	protected void testRole(User token, String expected) {
		Authentication authentication = mock(Authentication.class);
		UserAuthentication userAuthentication = new UserAuthentication(token, authentication);
		Collection<? extends GrantedAuthority> authorities = userAuthentication.getAuthorities();
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
		assertEquals(1, roles.size());
		assertTrue(roles.contains(expected));
	}

}
