package net.myconfig.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AppGrant;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.UserGrant;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class HubAccessDecisionManagerTest {

	private SecuritySelector selector;
	private HubAccessDecisionManager manager;

	@Before
	public void before() {
		selector = mock(SecuritySelector.class);
		manager = new HubAccessDecisionManager(selector);
	}

	@Test
	public void supports_method_invocation() {
		assertTrue(manager.supports(MethodInvocation.class));
	}

	@Test
	public void supports_any_attribute() {
		assertTrue(manager.supports(mock(ConfigAttribute.class)));
	}

	@Test
	public void checkUserGrant() {
		Authentication authentication = mock(Authentication.class);
		UserFunction fn = UserFunction.app_create;
		when(selector.hasUserFunction(authentication, fn)).thenReturn(true);
		assertTrue(manager.checkUserGrant(authentication, fn));
		verify(selector, times(1)).hasUserFunction(authentication, fn);
	}

	@Test
	public void checkApplicationGrant() {
		Authentication authentication = mock(Authentication.class);
		AppFunction fn = AppFunction.app_view;
		when(selector.hasApplicationFunction(authentication, "A", fn)).thenReturn(true);
		assertTrue(manager.checkApplicationGrant(authentication, "A", fn));
		verify(selector, times(1)).hasApplicationFunction(authentication, "A", fn);
	}

	@Test
	public void annotation_at_interface_level() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("user_call");
		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);

		UserGrant a = manager.getAnnotation(invocation, UserGrant.class);

		assertNotNull(a);
		assertEquals(UserFunction.app_list, a.value());
	}

	@Test
	public void annotation_at_implementation_level() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("app_call", Integer.TYPE);

		Object target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);

		AppGrant a = manager.getAnnotation(invocation, AppGrant.class);

		assertNotNull(a);
		assertEquals(AppFunction.app_view, a.value());
	}

	@Test(expected = AccessDeniedException.class)
	public void decide_no_constraint() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("no_constraint");

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);

		Authentication authentication = mock(Authentication.class);

		manager.decide(authentication, invocation, null);
	}

	@Test
	public void decide_user_ok() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("user_call");

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);

		Authentication authentication = mock(Authentication.class);
		when(selector.hasUserFunction(authentication, UserFunction.app_list)).thenReturn(true);

		manager.decide(authentication, invocation, null);
	}

	@Test
	public void decide_app_ok() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("app_call", Integer.TYPE);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new String[] { "A" });

		Authentication authentication = mock(Authentication.class);
		when(selector.hasApplicationFunction(authentication, "A", AppFunction.app_view)).thenReturn(true);

		manager.decide(authentication, invocation, null);
	}

	@Test(expected = EnvGrantParamMissingException.class)
	public void decide_env_no_param() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("env_call_missing_param", Integer.TYPE);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new Object[] { 1 });

		Authentication authentication = mock(Authentication.class);

		manager.decide(authentication, invocation, null);
	}

	@Test(expected = EnvGrantParamMissingException.class)
	public void decide_env_no_annotation() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("env_call_no_annotation", Integer.TYPE, String.class);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new Object[] { 1, "DEV" });

		Authentication authentication = mock(Authentication.class);

		manager.decide(authentication, invocation, null);
	}

	@Test(expected = GrantParamAlreadyDefinedException.class)
	public void decide_env_too_much() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("env_call_too_much", Integer.TYPE, String.class, String.class);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new Object[] { 1, "DEV", "x" });

		Authentication authentication = mock(Authentication.class);

		manager.decide(authentication, invocation, null);
	}

	@Test
	public void decide_env_ok() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("env_call_ok", Integer.TYPE, String.class);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new Object[] { "A", "DEV" });

		Authentication authentication = mock(Authentication.class);
		when(selector.hasEnvironmentFunction(authentication, "A", "DEV", EnvFunction.env_view)).thenReturn(true);

		manager.decide(authentication, invocation, null);
	}

	@Test(expected = AccessDeniedException.class)
	public void decide_env_not_granted() throws SecurityException, NoSuchMethodException {
		Method method = SampleAPI.class.getMethod("env_call_ok", Integer.TYPE, String.class);

		SampleImpl target = new SampleImpl();

		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getThis()).thenReturn(target);
		when(invocation.getArguments()).thenReturn(new Object[] { 1, "DEV" });

		Authentication authentication = mock(Authentication.class);
		when(selector.hasEnvironmentFunction(authentication, "A", "DEV", EnvFunction.env_view)).thenReturn(false);

		manager.decide(authentication, invocation, null);
	}

}
