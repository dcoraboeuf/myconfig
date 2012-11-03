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
import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.support.UserBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class BuiltinSecurityManagementTest {

	private static final String[] APPLICATIONS = { "A", "BB", "CCC" };
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

	@Before
	public void cleanContext() {
		// No context
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS)));
		SecurityContextHolder.setContext(context);
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
	public void getCurrentProfile_no_authentication() {
		SecurityContext context = mock(SecurityContext.class);
		SecurityContextHolder.setContext(context);
		User user = mgr.getCurrentProfile();
		assertNull(user);
	}
	
	@Test
	public void getCurrentProfile_no_user() {
		User user = mgr.getCurrentProfile();
		assertNull(user);
	}
	
	@Test
	public void getCurrentProfile_user() {
		User expectedUser = asUser();
		User actualUser = mgr.getCurrentProfile();
		assertSame(expectedUser, actualUser);
	}

	protected User asUser() {
		User expectedUser = UserBuilder.user();
		return asUser(expectedUser);
	}

	protected User asUser(User user) {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		SecurityContext context = mock(SecurityContext.class);
		when(context.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(context);
		return user;
	}
	
	@Test
	public void isLogged_no_user() {
		assertFalse(mgr.isLogged());
	}
	
	@Test
	public void isLogged_user() {
		asUser();
		assertTrue(mgr.isLogged());
	}
	
	@Test
	public void getCurrentUserName_no_user() {
		assertNull(mgr.getCurrentUserName());
	}
	
	@Test
	public void getCurrentUserName_user() {
		asUser(UserBuilder.user("user"));
		assertEquals("user", mgr.getCurrentUserName());
	}
	
	@Test
	public void hasOneOfUserFunction_no_user() {
		assertFalse(mgr.hasOneOfUserFunction());
	}
	
	@Test
	public void hasOneOfUserFunction_admin() {
		asUser(UserBuilder.create("admin").admin().build());
		assertTrue(mgr.hasOneOfUserFunction());
	}
	
	@Test
	public void hasOneOfUserFunction_no_fn() {
		asUser();
		assertFalse(mgr.hasOneOfUserFunction());
	}
	
	@Test
	public void hasOneOfUserFunction_at_least_one_function() {
		asUser(UserBuilder.create("user").build());
		when(grantService.hasUserFunction("user", UserFunction.app_list)).thenReturn(true);
		assertTrue(mgr.hasOneOfUserFunction(UserFunction.app_create, UserFunction.app_list));
	}
	
	@Test
	public void hasOneOfUserFunction_no_function() {
		asUser(UserBuilder.create("user").build());
		assertFalse(mgr.hasOneOfUserFunction(UserFunction.app_create, UserFunction.app_list));
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
	public void hasUserFunction_admin() {
		User user = UserBuilder.administrator();
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (UserFunction fn : UserFunction.values()) {
			assertEquals(String.format("Check for %s", fn), true, mgr.hasUserFunction(authentication, fn));
		}
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
	public void hasAppFunction_admin() {
		User user = UserBuilder.administrator();
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (String application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertEquals(true, mgr.hasApplicationFunction(authentication, application, fn));
			}
		}
	}

	@Test
	public void hasAppFunction_user() {
		User user = UserBuilder.create("user").build();		
		when(grantService.hasAppFunction("BB", "user", AppFunction.app_view)).thenReturn(true);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (String application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertEquals("BB".equals(application) && fn == AppFunction.app_view, mgr.hasApplicationFunction(authentication, application, fn));
			}
		}
	}

	@Test
	public void hasAppFunction_none() {
		for (String application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertFalse(mgr.hasApplicationFunction(null, application, fn));
			}
		}
	}

	@Test
	public void hasEnvFunction_admin() {
		User user = UserBuilder.administrator();
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (String application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertEquals(true, mgr.hasEnvironmentFunction(authentication, application, environment, fn));
				}
			}
		}
	}

	@Test
	public void hasEnvFunction_user() {
		User user = UserBuilder.create("user").build();	
		when(grantService.hasEnvFunction("BB", "user", "UAT", EnvFunction.env_view)).thenReturn(true);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(user);
		
		for (String application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertEquals("BB".equals(application) && "UAT".equals(environment) && fn == EnvFunction.env_view, mgr.hasEnvironmentFunction(authentication, application, environment, fn));
				}
			}
		}
	}

	@Test
	public void hasEnvFunction_none() {
		for (String application : APPLICATIONS) {
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
