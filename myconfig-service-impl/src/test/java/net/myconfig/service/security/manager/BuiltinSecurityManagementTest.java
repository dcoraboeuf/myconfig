package net.myconfig.service.security.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.UserProfile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class BuiltinSecurityManagementTest {

	private BuiltinSecurityManagement mgr;
	private AuthenticationService authenticationService;

	@Before
	public void init() {
		authenticationService = mock(AuthenticationService.class);
		mgr = new BuiltinSecurityManagement(authenticationService);
	}

	@Test
	public void getUserProfile() {
		UserProfile profile = mock(UserProfile.class);
		when(authenticationService.getUserToken("name", "pwd")).thenReturn(profile);
		UserProfile actualProfile = mgr.getUserToken("name", "pwd");
		assertSame(profile, actualProfile);
		verify(authenticationService, times(1)).getUserToken("name", "pwd");
	}

	@Test
	public void authenticate_no() {
		Authentication authentication = mock(Authentication.class);

		UserProfile actualProfile = mgr.authenticate(authentication);
		assertNull(actualProfile);
		verify(authenticationService, never()).getUserToken(anyString(), anyString());
	}

	@Test
	public void authenticate() {
		UserProfile profile = mock(UserProfile.class);
		when(authenticationService.getUserToken("name", "pwd")).thenReturn(profile);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("name", "pwd");

		UserProfile actualProfile = mgr.authenticate(authentication);

		assertSame(profile, actualProfile);
		verify(authenticationService, times(1)).getUserToken("name", "pwd");
	}

	@Test
	public void supports() {
		assertTrue(mgr.supports(UsernamePasswordAuthenticationToken.class));
		assertFalse(mgr.supports(Authentication.class));
	}

}
