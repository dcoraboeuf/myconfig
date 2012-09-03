package net.myconfig.service.api.security;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

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

}
