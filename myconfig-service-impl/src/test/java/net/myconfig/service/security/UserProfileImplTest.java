package net.myconfig.service.security;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.EnumSet;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.User;
import net.myconfig.service.api.security.UserProfile;

import org.junit.Test;

/**
 * Unit test for {@link UserProfileImpl}.
 */
public class UserProfileImplTest {

	private static final int[] APPLICATIONS = { 1, 2, 10 };
	private static final String[] ENVIRONMENTS = { "DEV", "TEST", "UAT", "PROD" };

	@Test
	public void name() {
		UserProfile profile = emptyUser("xxx");
		assertEquals("xxx", profile.getName());
	}

	@Test
	public void displayName() {
		UserProfile profile = emptyUser("xxx");
		assertEquals("xxx", profile.getDisplayName());
	}

	@Test
	public void admin_yes() {
		UserProfile profile = emptyUser("xxx", true);
		assertTrue(profile.isAdmin());
	}

	@Test
	public void admin_no() {
		UserProfile profile = emptyUser("xxx", false);
		assertFalse(profile.isAdmin());
	}

	@Test
	public void hasUserFunction_admin() {
		UserProfile profile = emptyUser("xxx", true);
		for (UserFunction fn : UserFunction.values()) {
			assertTrue(profile.hasUserFunction(fn));
		}
	}

	@Test
	public void hasUserFunction_user() {
		UserProfile profile = user("xxx", UserFunction.app_create);
		for (UserFunction fn : UserFunction.values()) {
			assertEquals(fn == UserFunction.app_create, profile.hasUserFunction(fn));
		}
	}

	@Test
	public void hasAppFunction_admin() {
		UserProfile profile = emptyUser("xxx", true);
		for (int application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertTrue(profile.hasAppFunction(application, fn));
			}
		}
	}

	@Test
	public void hasAppFunction_user() {
		UserProfile profile = user("xxx", 2, AppFunction.app_view);
		for (int application : APPLICATIONS) {
			for (AppFunction fn : AppFunction.values()) {
				assertEquals(application == 2 && fn == AppFunction.app_view, profile.hasAppFunction(application, fn));
			}
		}
	}

	@Test
	public void hasEnvFunction_admin() {
		UserProfile profile = emptyUser("xxx", true);
		for (int application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertTrue(profile.hasEnvFunction(application, environment, fn));
				}
			}
		}
	}

	@Test
	public void hasEnvFunction_user() {
		UserProfile profile = user("xxx", 2, "UAT", EnvFunction.env_view);
		for (int application : APPLICATIONS) {
			for (String environment : ENVIRONMENTS) {
				for (EnvFunction fn : EnvFunction.values()) {
					assertEquals(application == 2 && "UAT".equals(environment) && fn == EnvFunction.env_view, profile.hasEnvFunction(application, environment, fn));
				}
			}
		}
	}

	private UserProfile emptyUser(String name) {
		return emptyUser(name, false);
	}

	private UserProfile user(String name, UserFunction fn) {
		User user = new User(name, false, true);
		UserProfile profile = new UserProfileImpl(user, EnumSet.of(fn), Collections.<AppFunctionKey> emptySet(), Collections.<EnvFunctionKey> emptySet());
		return profile;
	}

	private UserProfile user(String name, int application, AppFunction fn) {
		User user = new User(name, false, true);
		UserProfile profile = new UserProfileImpl(user, EnumSet.noneOf(UserFunction.class), singleton(new AppFunctionKey(application, fn)), Collections.<EnvFunctionKey> emptySet());
		return profile;
	}

	private UserProfile user(String name, int application, String environment, EnvFunction fn) {
		User user = new User(name, false, true);
		UserProfile profile = new UserProfileImpl(user, EnumSet.noneOf(UserFunction.class), Collections.<AppFunctionKey> emptySet(), singleton(new EnvFunctionKey(application, environment, fn)));
		return profile;
	}

	private UserProfile emptyUser(String name, boolean admin) {
		User user = new User(name, admin, true);
		UserProfile profile = new UserProfileImpl(user, EnumSet.noneOf(UserFunction.class), Collections.<AppFunctionKey> emptySet(), Collections.<EnvFunctionKey> emptySet());
		return profile;
	}

}
