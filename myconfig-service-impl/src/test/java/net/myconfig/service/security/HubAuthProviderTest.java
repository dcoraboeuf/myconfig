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
import net.myconfig.service.api.security.UserProfile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

public class HubAuthProviderTest {

	private HubAuthProvider provider;
	private SecuritySelector selector;

	@Before
	public void before() {
		selector = mock(SecuritySelector.class);
		provider = new HubAuthProvider(selector);
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
		UserProfile userToken = mock(UserProfile.class);
		when(selector.authenticate(authentication)).thenReturn(userToken);

		Authentication o = provider.authenticate(authentication);
		assertNotNull(o);
		assertTrue(o instanceof UserAuthentication);
		UserAuthentication a = (UserAuthentication) o;
		assertSame(userToken, a.getDetails());
		verify(selector, times(1)).authenticate(authentication);
	}

}
