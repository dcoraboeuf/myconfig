package net.myconfig.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserProfile;
import net.myconfig.service.security.UserAuthentication;
import net.myconfig.test.AbstractIntegrationTest;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({ "classpath:META-INF/secure-aop.xml" })
public abstract class AbstractSecurityTest extends AbstractIntegrationTest {
	
	@Autowired
	protected SecurityService securityService;
	
	@Autowired
	protected SecuritySelector securitySelector;

	@Before
	public void cleanContext() {
		// Sets the security mode
		asAdmin();
		securityService.setSecurityMode("builtin");
		// No context
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS)));
		SecurityContextHolder.setContext(context);
	}

	protected void asUser(final UserFunction... functions) {
		SecurityContextImpl context = new SecurityContextImpl();

		UserProfile token = mock(UserProfile.class);
		when(token.hasUserFunction(any(UserFunction.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				UserFunction fn = (UserFunction) invocation.getArguments()[0];
				return ArrayUtils.contains(functions, fn);
			}
		});

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(token, authentication));
		SecurityContextHolder.setContext(context);
	}

	protected void asUser(int application, AppFunction fn) {
		SecurityContextImpl context = new SecurityContextImpl();

		UserProfile token = mock(UserProfile.class);
		when(token.hasAppFunction(application, fn)).thenReturn(true);

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(token, authentication));
		SecurityContextHolder.setContext(context);
	}

	protected void asUser(int application, EnvFunction fn, String... envs) {
		SecurityContextImpl context = new SecurityContextImpl();

		UserProfile token = mock(UserProfile.class);
		when(token.hasAppFunction(application, AppFunction.app_view)).thenReturn(true);
		for (String env : envs) {
			when(token.hasEnvFunction(application, env, fn)).thenReturn(true);
		}

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(token, authentication));
		SecurityContextHolder.setContext(context);
	}

	protected void asAdmin() {
		SecurityContextImpl context = new SecurityContextImpl();
		
		UserProfile token = mock(UserProfile.class);
		when(token.isAdmin()).thenReturn(true);
		when(token.hasUserFunction(any(UserFunction.class))).thenReturn(true);
		when(token.hasAppFunction(anyInt(), any(AppFunction.class))).thenReturn(true);
		when(token.hasEnvFunction(anyInt(), anyString(), any(EnvFunction.class))).thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(token, authentication));
		SecurityContextHolder.setContext(context);
	}
}
