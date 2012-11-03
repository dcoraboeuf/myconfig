package net.myconfig.service.impl;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.core.model.Message;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.message.TestPost;
import net.myconfig.service.security.UserAuthentication;
import net.myconfig.test.AbstractIntegrationTest;

import org.junit.Before;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({ "classpath:META-INF/secure-aop.xml" })
public abstract class AbstractSecurityTest extends AbstractIntegrationTest {

	private final Logger logger = LoggerFactory.getLogger(AbstractSecurityTest.class);

	protected class UserGrant {

		private final String name;

		public UserGrant(String name) {
			this.name = name;
		}

		public UserGrant grant(UserFunction fn) throws SQLException {
			execute("insert into usergrants (user, grantedfunction) values (?, ?)", name, fn.name());
			return this;
		}

		public UserGrant grant(String application, AppFunction fn) throws SQLException {
			execute("insert into appgrants (user, application, grantedfunction) values (?, ?, ?)", name, application, fn.name());
			return this;
		}

		public UserGrant grant(String application, String environment, EnvFunction fn) throws SQLException {
			execute("insert into envgrants (user, application, environment, grantedfunction) values (?, ?, ?, ?)", name, application, environment, fn.name());
			return this;
		}
		
		public String getName() {
			return name;
		}

	}

	@Autowired
	protected SecurityService securityService;

	@Autowired
	protected SecuritySelector securitySelector;

	@Autowired
	protected AuthenticationService authenticationService;
	
	@Autowired
	protected MyConfigService myConfigService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	protected TestPost post;

	private static final AtomicInteger userId = new AtomicInteger(100);
	private static final AtomicInteger appId = new AtomicInteger(100);
	private static final AtomicInteger appName = new AtomicInteger(100);

	@Before
	public void cleanContext() throws SQLException {
		// Makes sure the initial security manager is set to 'builtin'
		asAdmin();
		securityService.setSecurityMode("builtin");
		// Clears all caches
		Collection<String> names = cacheManager.getCacheNames();
		for (String name : names) {
			cacheManager.getCache(name).clear();
		}
		// No context
		anonymous();
	}
	
	protected void anonymous() {
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList(MyConfigRoles.ANONYMOUS)));
		SecurityContextHolder.setContext(context);
	}

	protected void asAdmin() throws SQLException {
		User user = new User("admin", "Administrator", "admin@myconfig.net", true, true, false);
		clearGrants("admin");
		asUser(user);
	}

	protected UserGrant asUser() throws SQLException {
		return asUser(userName());
	}

	protected String userName() {
		String name = "user" + userId.incrementAndGet();
		logger.debug("** User name: {}", name);
		return name;
	}

	protected String appId() {
		String name = "app" + appId.incrementAndGet();
		logger.debug("** App ID: {}", name);
		return name;
	}

	protected String appName() {
		String name = "appName" + appName.incrementAndGet();
		logger.debug("** App name: {}", name);
		return name;
	}

	protected UserGrant asUser(String name) throws SQLException {
		User user = new User(name, "User", userEmail(name), false, true, false);
		clearGrants(name);
		asUser(user);
		return new UserGrant(name);
	}

	private void clearGrants(String name) throws SQLException {
		execute("delete from usergrants where user = ?", name);
		execute("delete from appgrants where user = ?", name);
		execute("delete from envgrants where user = ?", name);
	}

	protected String createUser() throws SQLException {
		asAdmin();
		String user = userName();
		securityService.userCreate(user, "User", userEmail(user));
		return user;
	}

	protected String userEmail(String user) {
		return user + "@test.com";
	}

	private void asUser(User user) {
		SecurityContextImpl context = new SecurityContextImpl();

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(user, authentication));
		SecurityContextHolder.setContext(context);
	}
	
	protected void verifyAndLog (String name, String password) throws SQLException {
		verify(name, password);
		// Logs
		asUser(name);
	}

	protected void verify(String name, String password) {
		// Gets the last message for this user
		Message message = post.getMessage(userEmail(name));
		assertNotNull(message);
		String token = message.getContent().getToken();
		// Verification
		anonymous();
		securityService.userConfirm(name, token, password);
	}
}
