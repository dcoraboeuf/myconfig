package net.myconfig.service.security.support;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.security.UserProfile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class SecurityUtilsTest {

	private Authentication authentication;

	@Before
	public void cleanContext() {
		SecurityContextImpl context = new SecurityContextImpl();
		authentication = mock(Authentication.class);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	@Test
	public void authentication() {
		assertSame(authentication, SecurityUtils.authentication());
	}

	@Test
	public void profile_no_authentication() {
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(null);
		SecurityContextHolder.setContext(context);
		assertNull(SecurityUtils.profile());
	}

	@Test
	public void profile_no_profile() {
		when(authentication.getDetails()).thenReturn("xxx");
		assertNull(SecurityUtils.profile());
	}

	@Test
	public void profile_ok() {
		UserProfile profile = mock(UserProfile.class);
		when(authentication.getDetails()).thenReturn(profile);
		assertSame(profile, SecurityUtils.profile());
	}

}
