package net.myconfig.service.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserToken;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;

public class HubAuthProviderTest {

	private HubAuthProvider provider;
	private SecuritySelector selector;

	@Before
	public void before() {
		selector = mock(SecuritySelector.class);
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(SecuritySelector.class)).thenReturn(selector);
		provider = new HubAuthProvider(applicationContext);
	}

	@Test
	public void supports() {
		provider.supports(Authentication.class);
		verify(selector, times(1)).supports(Authentication.class);
	}

	@Test
	public void authenticate_no_token() {
		Authentication authentication = mock(Authentication.class);
		Authentication o = provider.authenticate(authentication);
		assertNull(o);
		verify(selector, times(1)).authenticate(authentication);
	}

	@Test
	public void authenticate_found() {
		Authentication authentication = mock(Authentication.class);
		UserToken userToken = mock(UserToken.class);
		when(selector.authenticate(authentication)).thenReturn(userToken);

		Authentication o = provider.authenticate(authentication);
		assertNotNull(o);
		assertTrue(o instanceof UserAuthenticationToken);
		UserAuthenticationToken a = (UserAuthenticationToken) o;
		assertSame(userToken, a.getDetails());
		verify(selector, times(1)).authenticate(authentication);
	}

}
