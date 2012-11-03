package net.myconfig.service.security;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.User;
import net.myconfig.service.support.UserBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

public class HubSecuritySelectorTest {

	private ConfigurationService configurationService;
	private HubSecuritySelector selector;
	private SecurityManagement manager1, manager2;

	@Before
	public void before() {
		configurationService = mock(ConfigurationService.class);

		manager1 = mock(SecurityManagement.class);
		when(manager1.getId()).thenReturn("manager1");

		manager2 = mock(SecurityManagement.class);
		when(manager2.getId()).thenReturn("manager2");

		selector = new HubSecuritySelector(asList(manager1, manager2), configurationService);
	}

	@Test
	public void getSecurityManagementId() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("xxx");
		assertEquals("xxx", selector.getSecurityMode());
	}

	@Test
	public void getSecurityModes() {
		assertEquals(asList("manager1", "manager2"), selector.getSecurityModes());
	}

	@Test
	public void getSecurityManagement_1() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager1");
		SecurityManagement securityManagement = selector.getSecurityManagement();
		assertTrue(securityManagement == manager1);
	}

	@Test
	public void getSecurityManagement_2() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");
		SecurityManagement securityManagement = selector.getSecurityManagement();
		assertTrue(securityManagement == manager2);
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void getSecurityManagement_not_found() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager3");
		selector.getSecurityManagement();
	}

	@Test
	public void delegate_authenticate() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		User expectedUser = UserBuilder.user();
		when(manager2.authenticate(authentication)).thenReturn(expectedUser);

		User actualUser = selector.authenticate(authentication);

		assertSame(expectedUser, actualUser);
	}

	@Test
	public void delegate_supports() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		selector.supports(Authentication.class);

		verify(manager2, times(1)).supports(Authentication.class);
	}

	@Test
	public void delegate_hasUserFunction() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		when(manager2.hasUserFunction(authentication, UserFunction.security_setup)).thenReturn(true);

		boolean actual = selector.hasUserFunction(authentication, UserFunction.security_setup);

		assertTrue(actual);
	}

	@Test
	public void delegate_hasAppFunction() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		when(manager2.hasApplicationFunction(authentication, "A", AppFunction.app_users)).thenReturn(true);

		boolean actual = selector.hasApplicationFunction(authentication, "A", AppFunction.app_users);

		assertTrue(actual);
	}

	@Test
	public void delegate_hasEnvFunction() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		when(manager2.hasEnvironmentFunction(authentication, "A", "UAT", EnvFunction.env_config)).thenReturn(true);

		boolean actual = selector.hasEnvironmentFunction(authentication, "A", "UAT", EnvFunction.env_config);

		assertTrue(actual);
	}

	@Test
	public void delegate_allowLogin() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		when(manager2.allowLogin()).thenReturn(true);

		boolean actual = selector.allowLogin();

		assertTrue(actual);
	}
	
	@Test
	public void delegate_isLogged() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		when(manager2.isLogged()).thenReturn(true);

		boolean actual = selector.isLogged();

		assertTrue(actual);
	}
	
	@Test
	public void delegate_hasOneOfUserFunction() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		when(manager2.hasOneOfUserFunction()).thenReturn(true);

		boolean actual = selector.hasOneOfUserFunction();

		assertTrue(actual);
	}
	
	@Test
	public void delegate_getCurrentProfile() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		User user = UserBuilder.user();
		when(manager2.getCurrentProfile()).thenReturn(user);

		User actual = selector.getCurrentProfile();

		assertSame(user, actual);
	}
	
	@Test
	public void delegate_getCurrentUserName() {
		when(configurationService.getParameter(ConfigurationKey.SECURITY_MODE)).thenReturn("manager2");

		when(manager2.getCurrentUserName()).thenReturn("user");

		String actual = selector.getCurrentUserName();

		assertEquals("user", actual);
	}

}
