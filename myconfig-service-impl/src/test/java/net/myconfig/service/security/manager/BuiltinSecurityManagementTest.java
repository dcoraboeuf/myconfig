package net.myconfig.service.security.manager;

import static org.junit.Assert.assertEquals;
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
import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.support.UserBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class BuiltinSecurityManagementTest {

	private static final int[] APPLICATIONS = { 1, 2, 10 };
	private static final String[] ENVIRONMENTS = { "DEV", "TEST", "UAT", "PROD" };

	private BuiltinSecurityManagement mgr;
	private AuthenticationService authenticationService;
	private GrantService grantService;

	@Before
	public void init() {
		authenticationService = mock(AuthenticationService.class);
		grantService = mock(GrantService.class);
		mgr = new BuiltinSecurityManagement(authenticationService, grantService);
	}

	@Test
	public void getUserProfile() {
		User expectedUser = mock(User.class);
		when(authenticationService.getUserToken("name", "pwd")).thenReturn(expectedUser);
		User actualUser = mgr.getUserToken("name", "pwd");
		assertSame(expectedUser, actualUser);
		verify(authenticationService, times(1)).getUserToken("name", "pwd");
	}

	@Test
	public void authenticate_no() {
		Authentication authentication = mock(Authentication.class);

		User actualUser = mgr.authenticate(authentication);
		assertNull(actualUser);
		verify(authenticationService, never()).getUserToken(anyString(), anyString());
	}

	@Test
	public void authenticate() {
		User expectedUser = mock(User.class);
		when(authenticationService.getUserToken("name", "pwd")).thenReturn(expectedUser);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("name", "pwd");

		User actualUser = mgr.authenticate(authentication);

		assertSame(expectedUser, actualUser);
		verify(authenticationService, times(1)).getUserToken("name", "pwd");
	}

	@Test
	public void supports() {
		assertTrue(mgr.supports(UsernamePasswordAuthenticationToken.class));
		assertFalse(mgr.supports(Authentication.class));
	}

	@Test
	public void getUserToken_no_authentication() {
		assertNull(mgr.getUserToken(null));
	}

	@Test
	public void getUserToken_no_detail() {
		Authentication authentication = mock(Authentication.class);
		assertNull(mgr.getUserToken(authentication));
	}

	@Test
	public void getUserToken_profile() {
		User user = mock(User.class);
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		assertSame(user, mgr.getUserToken(authentication));
	}

	@Test
	public void hasUserFunction_user() {
		User user = UserBuilder.create("user").build();		
		when(grantService.hasUserFunction("user", UserFunction.app_create)).thenReturn(true);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		for (UserFunction fn : UserFunction.values()) {
			assertEquals(String.format("Check for %s", fn), fn == UserFunction.app_create, mgr.hasUserFunction(authentication, fn));
		}
	}

	@Test
	public void hasUserFunction_none() {
		for (UserFunction fn : UserFunction.values()) {
			assertFalse(String.format("Check for %s", fn), mgr.hasUserFunction(null, fn));
		}
	}

	@Test
	public void hasAppFunction_user() {
		User user = UserBuilder.create("user").build();		
		when(grantService.hasAppFunction("user", 2, AppFunction.app_view)).thenReturn(true);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (int application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertEquals(application == 2 && fn == AppFunction.app_view, mgr.hasApplicationFunction(authentication, application, fn));
			}
		}
	}

	@Test
	public void hasAppFunction_none() {
		for (int application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertFalse(mgr.hasApplicationFunction(null, application, fn));
			}
		}
	}

	@Test
	public void hasEnvFunction_user() {
		User user = UserBuilder.create("user").build();	
		when(grantService.hasEnvFunction("user", 2, "UAT", EnvFunction.env_view)).thenReturn(true);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (int application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertEquals(application == 2 && "UAT".equals(environment) && fn == EnvFunction.env_view, mgr.hasEnvironmentFunction(authentication, application, environment, fn));
				}
			}
		}
	}

	@Test
	public void hasEnvFunction_none() {
		for (int application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertFalse(mgr.hasEnvironmentFunction(null, application, environment, fn));
				}
			}
		}
	}

	@Test
	public void allowLogin() {
		assertTrue(mgr.allowLogin());
	}

}
