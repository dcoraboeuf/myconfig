package net.myconfig.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.MyConfigRoles;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.AuthenticationService;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.User;
import net.myconfig.service.security.UserAuthentication;
import net.myconfig.test.AbstractIntegrationTest;
import net.myconfig.test.DBUnitHelper;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

		public UserGrant grant(int application, AppFunction fn) throws SQLException {
			execute("insert into appgrants (user, application, grantedfunction) values (?, ?, ?)", name, application, fn.name());
			return this;
		}

		public UserGrant grant(int application, String environment, EnvFunction fn) throws SQLException {
			execute("insert into envgrants (user, application, environment, grantedfunction) values (?, ?, ?, ?)", name, application, environment, fn.name());
			return this;
		}

	}

	@Autowired
	protected SecurityService securityService;

	@Autowired
	protected SecuritySelector securitySelector;

	@Autowired
	protected AuthenticationService authenticationService;

	@Before
	public void cleanContext() {
		// No context
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
		return asUser("user");
	}

	protected UserGrant asUser(String name) throws SQLException {
		User user = new User(name, "User", "user@myconfig.net", false, true, false);
		clearGrants(name);
		asUser(user);
		return new UserGrant(name);
	}

	private void clearGrants(String name) throws SQLException {
		execute("delete from usergrants where user = ?", name);
		execute("delete from appgrants where user = ?", name);
		execute("delete from envgrants where user = ?", name);
	}

	private void execute(String sql, Object... params) throws SQLException {
		logger.debug("SQL: {} {}", sql, StringUtils.join(params));
		Connection c = DBUnitHelper.getConnection().getConnection();
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			try {
				int index = 1;
				for (Object param : params) {
					ps.setObject(index++, param);
				}
				ps.executeUpdate();
			} finally {
				ps.close();
			}
		} finally {
			c.commit();
		}
	}

	private void asUser(User user) {
		SecurityContextImpl context = new SecurityContextImpl();

		Authentication authentication = Mockito.mock(Authentication.class);
		context.setAuthentication(new UserAuthentication(user, authentication));
		SecurityContextHolder.setContext(context);
	}
}
