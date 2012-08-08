package net.myconfig.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.myconfig.core.AppFunction;

import org.junit.Test;

public class AppFunctionKeyTest {

	@Test
	public void equals_not_instance() {
		assertFalse("1/app_view".equals(new AppFunctionKey(1, AppFunction.app_view)));
	}

	@Test
	public void equals_different_application() {
		assertFalse(new AppFunctionKey(2, AppFunction.app_view).equals(new AppFunctionKey(1, AppFunction.app_view)));
	}

	@Test
	public void equals_different_fn() {
		assertFalse(new AppFunctionKey(1, AppFunction.app_delete).equals(new AppFunctionKey(1, AppFunction.app_view)));
	}

	@Test
	public void equals_ok() {
		assertTrue(new AppFunctionKey(1, AppFunction.app_view).equals(new AppFunctionKey(1, AppFunction.app_view)));
	}

	@Test
	public void testHashCode() {
		int a = new AppFunctionKey(1, AppFunction.app_view).hashCode();
		int b = new AppFunctionKey(1, AppFunction.app_view).hashCode();
		assertEquals(a, b);
		assertTrue(a != 0);
	}

	@Test
	public void testToString() {
		String s = new AppFunctionKey(1, AppFunction.app_view).toString();
		assertEquals("1/app_view", s);
	}
}
