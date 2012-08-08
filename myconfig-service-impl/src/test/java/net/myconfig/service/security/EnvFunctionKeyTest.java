package net.myconfig.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.myconfig.core.EnvFunction;

import org.junit.Test;

public class EnvFunctionKeyTest {

	@Test
	public void equals_not_instance() {
		assertFalse("1/UAT/env_view".equals(new EnvFunctionKey(1, "UAT", EnvFunction.env_view)));
	}

	@Test
	public void equals_different_application() {
		assertFalse(new EnvFunctionKey(2, "UAT", EnvFunction.env_view).equals(new EnvFunctionKey(1, "UAT", EnvFunction.env_view)));
	}

	@Test
	public void equals_different_environment() {
		assertFalse(new EnvFunctionKey(1, "DEV", EnvFunction.env_view).equals(new EnvFunctionKey(1, "UAT", EnvFunction.env_view)));
	}

	@Test
	public void equals_different_fn() {
		assertFalse(new EnvFunctionKey(1, "UAT", EnvFunction.env_config).equals(new EnvFunctionKey(1, "UAT", EnvFunction.env_view)));
	}

	@Test
	public void equals_ok() {
		assertTrue(new EnvFunctionKey(1, "UAT", EnvFunction.env_view).equals(new EnvFunctionKey(1, "UAT", EnvFunction.env_view)));
	}

	@Test
	public void testHashCode() {
		int a = new EnvFunctionKey(1, "UAT", EnvFunction.env_view).hashCode();
		int b = new EnvFunctionKey(1, "UAT", EnvFunction.env_view).hashCode();
		assertEquals(a, b);
		assertTrue(a != 0);
	}

	@Test
	public void testToString() {
		String s = new EnvFunctionKey(1, "UAT", EnvFunction.env_view).toString();
		assertEquals("1/UAT/env_view", s);
	}
}
