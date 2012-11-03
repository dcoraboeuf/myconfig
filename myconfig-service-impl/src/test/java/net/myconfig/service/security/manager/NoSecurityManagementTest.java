package net.myconfig.service.security.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;

import org.junit.Test;
import org.springframework.security.core.Authentication;

public class NoSecurityManagementTest {

	private static final String[] APPLICATIONS = { "A", "BB", "CCC" };
	private static final String[] ENVIRONMENTS = { "DEV", "TEST", "UAT", "PROD" };

	private final NoSecurityManagement mgr = new NoSecurityManagement();

	@Test
	public void authenticate() {
		Authentication authentication = mock(Authentication.class);
		User profile = mgr.authenticate(authentication);
		assertNotNull(profile);
		assertEquals("anonymous", profile.getName());
	}
	
	@Test
	public void getCurrentUserName() {
		assertNull(mgr.getCurrentUserName());
	}
	
	@Test
	public void getCurrentProfile() {
		User user = mgr.getCurrentProfile();
		assertNotNull(user);
		assertTrue(user.isAdmin());
		assertEquals("anonymous", user.getName());
		assertEquals("Anonymous", user.getDisplayName());
		assertEquals("", user.getEmail());
	}
	
	@Test
	public void isLogged() {
		assertTrue(mgr.isLogged());
	}
	
	@Test
	public void hasOneOfUserFunction() {
		assertTrue(mgr.hasOneOfUserFunction());
	}

	@Test
	public void supports() {
		assertTrue(mgr.supports(Authentication.class));
	}

	@Test
	public void allowLogin() {
		assertFalse(mgr.allowLogin());
	}

	@Test
	public void hasUserFunction() {
		Authentication authentication = mock(Authentication.class);
		for (UserFunction fn : UserFunction.values()) {
			assertTrue(mgr.hasUserFunction(authentication, fn));
		}
	}

	@Test
	public void hasApplicationFunction() {
		Authentication authentication = mock(Authentication.class);
		for (String application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertTrue(mgr.hasApplicationFunction(authentication, application, fn));
			}
		}
	}

	@Test
	public void hasEnvFunction_admin() {
		Authentication authentication = mock(Authentication.class);
		for (String application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertTrue(mgr.hasEnvironmentFunction(authentication, application, environment, fn));
				}
			}
		}
	}

}
