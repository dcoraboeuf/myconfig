package net.myconfig.service.security;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserToken;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

public class HubSecuritySelectorTest {

	private SecurityService securityService;
	private HubSecuritySelector selector;
	private SecurityManagement manager1, manager2;

	@Before
	public void before() {
		securityService = mock(SecurityService.class);

		manager1 = mock(SecurityManagement.class);
		when(manager1.getId()).thenReturn("manager1");

		manager2 = mock(SecurityManagement.class);
		when(manager2.getId()).thenReturn("manager2");

		selector = new HubSecuritySelector(asList(manager1, manager2), securityService);
	}

	@Test
	public void getSecurityManagementId() {
		when(securityService.getSecurityMode()).thenReturn("xxx");
		assertEquals("xxx", selector.getSecurityManagementId());
	}

	@Test
	public void getSecurityModes() {
		assertEquals(asList("manager1", "manager2"), selector.getSecurityModes());
	}

	@Test
	public void getSecurityManagement_1() {
		when(securityService.getSecurityMode()).thenReturn("manager1");
		SecurityManagement securityManagement = selector.getSecurityManagement();
		assertTrue(securityManagement == manager1);
	}

	@Test
	public void getSecurityManagement_2() {
		when(securityService.getSecurityMode()).thenReturn("manager2");
		SecurityManagement securityManagement = selector.getSecurityManagement();
		assertTrue(securityManagement == manager2);
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void getSecurityManagement_not_found() {
		when(securityService.getSecurityMode()).thenReturn("manager3");
		selector.getSecurityManagement();
	}

	@Test
	public void delegate_authenticate() {
		when(securityService.getSecurityMode()).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		UserToken expectedUserToken = mock(UserToken.class);
		when(manager2.authenticate(authentication)).thenReturn(expectedUserToken);

		UserToken actualUserToken = selector.authenticate(authentication);

		assertSame(expectedUserToken, actualUserToken);
	}

	@Test
	public void delegate_supports() {
		when(securityService.getSecurityMode()).thenReturn("manager2");

		selector.supports(Authentication.class);

		verify(manager2, times(1)).supports(Authentication.class);
	}

	@Test
	public void delegate_hasUserFunction() {
		when(securityService.getSecurityMode()).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		when(manager2.hasUserFunction(authentication, UserFunction.security_setup)).thenReturn(true);

		boolean actual = selector.hasUserFunction(authentication, UserFunction.security_setup);

		assertTrue(actual);
	}

	@Test
	public void delegate_hasAppFunction() {
		when(securityService.getSecurityMode()).thenReturn("manager2");

		Authentication authentication = mock(Authentication.class);
		when(manager2.hasApplicationFunction(authentication, 10, AppFunction.app_users)).thenReturn(true);

		boolean actual = selector.hasApplicationFunction(authentication, 10, AppFunction.app_users);

		assertTrue(actual);
	}

	@Test
	public void delegate_allowLogin() {
		when(securityService.getSecurityMode()).thenReturn("manager2");

		when(manager2.allowLogin()).thenReturn(true);

		boolean actual = selector.allowLogin();

		assertTrue(actual);
	}

	@Test
	public void switchSecurityMode_same() {
		when(securityService.getSecurityMode()).thenReturn("manager1");
		assertEquals("manager1", selector.getSecurityManagementId());
		selector.switchSecurityMode("manager1");
		assertEquals("manager1", selector.getSecurityManagementId());
		verify(securityService, never()).setSecurityMode(anyString());
	}

	@Test
	public void switchSecurityMode_changed() {
		when(securityService.getSecurityMode()).thenReturn("manager1");
		assertEquals("manager1", selector.getSecurityManagementId());
		selector.switchSecurityMode("manager2");
		verify(securityService, times(1)).setSecurityMode("manager2");
	}

	@Test(expected = SecurityManagementNotFoundException.class)
	public void switchSecurityMode_unknown() {
		when(securityService.getSecurityMode()).thenReturn("manager1");
		assertEquals("manager1", selector.getSecurityManagementId());
		selector.switchSecurityMode("managerx");
	}

}
